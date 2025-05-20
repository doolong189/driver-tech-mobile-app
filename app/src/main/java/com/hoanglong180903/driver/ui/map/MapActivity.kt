package com.hoanglong180903.driver.ui.map

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.hoanglong180903.driver.common.base.BaseActivity
import com.hoanglong180903.driver.common.service.GpsTrackerService
import com.hoanglong180903.driver.common.service.Polyline
import com.hoanglong180903.driver.databinding.MapActivityBinding
import com.hoanglong180903.driver.utils.Constants
import com.hoanglong180903.driver.utils.SharedPreferences
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.ExperimentalPreviewMapboxNavigationAPI
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.extensions.applyLanguageAndVoiceUnitOptions
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.base.route.NavigationRoute
import com.mapbox.navigation.base.route.NavigationRouterCallback
import com.mapbox.navigation.base.route.RouteAlternativesOptions
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.route.RouterOrigin
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import com.mapbox.navigation.core.lifecycle.MapboxNavigationObserver
import com.mapbox.navigation.core.lifecycle.requireMapboxNavigation
import com.mapbox.navigation.core.preview.RoutesPreviewObserver
import com.mapbox.navigation.core.replay.route.ReplayProgressObserver
import com.mapbox.navigation.core.replay.route.ReplayRouteMapper
import com.mapbox.navigation.core.routealternatives.AlternativeRouteMetadata
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
import com.mapbox.navigation.ui.maps.NavigationStyles
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import com.mapbox.navigation.ui.maps.route.RouteLayerConstants.TOP_LEVEL_ROUTE_LINE_LAYER_ID
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowApi
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowView
import com.mapbox.navigation.ui.maps.route.arrow.model.RouteArrowOptions
import com.mapbox.navigation.ui.maps.route.callout.api.DefaultRouteCalloutAdapter
import com.mapbox.navigation.ui.maps.route.callout.model.DefaultRouteCalloutAdapterOptions
import com.mapbox.navigation.ui.maps.route.callout.model.RouteCalloutType
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineApiOptions
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineViewOptions
import com.mapbox.navigation.ui.maps.route.line.model.RouteLineColorResources
import java.util.Date
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
class MapActivity : BaseActivity<MapActivityBinding>() {
    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    private lateinit var preferences: SharedPreferences
    private lateinit var locationComponent: LocationComponentPlugin


    private lateinit var replayProgressObserver: ReplayProgressObserver

    private val navigationLocationProvider by lazy {
        NavigationLocationProvider()
    }

    private val routeLineViewOptions: MapboxRouteLineViewOptions by lazy {
        MapboxRouteLineViewOptions.Builder(this)
            .routeLineColorResources(RouteLineColorResources.Builder().build())
            .routeLineBelowLayerId("road-label-navigation")
            .build()
    }

    private val routeLineApiOptions: MapboxRouteLineApiOptions by lazy {
        MapboxRouteLineApiOptions.Builder()
            .vanishingRouteLineEnabled(true)
            .isRouteCalloutsEnabled(true)
            .build()
    }

    private val routeLineView by lazy {
        MapboxRouteLineView(routeLineViewOptions).also {
            it.setCalloutAdapter(binding.mapView.viewAnnotationManager, routeCalloutAdapter)
        }
    }
    private val routeLineApi: MapboxRouteLineApi by lazy {
        MapboxRouteLineApi(routeLineApiOptions)
    }
    private val routeArrowApi: MapboxRouteArrowApi by lazy {
        MapboxRouteArrowApi()
    }
    private val routeArrowOptions by lazy {
        RouteArrowOptions.Builder(this)
            .withAboveLayerId(TOP_LEVEL_ROUTE_LINE_LAYER_ID)
            .build()
    }
    private val routeArrowView: MapboxRouteArrowView by lazy {
        MapboxRouteArrowView(routeArrowOptions)
    }
    private val routeCalloutAdapterOptions: DefaultRouteCalloutAdapterOptions by lazy {
        DefaultRouteCalloutAdapterOptions.Builder()
            .similarDurationDelta(1.minutes)
            .routeCalloutType(RouteCalloutType.ROUTES_OVERVIEW)
            .build()
    }
    private val routeCalloutAdapter: DefaultRouteCalloutAdapter by lazy {
        DefaultRouteCalloutAdapter(this, routeCalloutAdapterOptions) { data ->
            reorderRoutes(data.route)
        }
    }
    private val routesObserver: RoutesObserver = RoutesObserver { routeUpdateResult ->
        updateRoutes(
            routeUpdateResult.navigationRoutes,
            mapboxNavigation.getAlternativeMetadataFor(routeUpdateResult.navigationRoutes)
        )
    }
    private val routesPreviewObserver: RoutesPreviewObserver = RoutesPreviewObserver { update ->
        val preview = update.routesPreview ?: return@RoutesPreviewObserver

        updateRoutes(preview.routesList, preview.alternativesMetadata)
    }

    private fun updateRoutes(routesList: List<NavigationRoute>, alternativesMetadata: List<AlternativeRouteMetadata>) {
        routeLineApi.setNavigationRoutes(routesList, alternativesMetadata) { value ->
            binding.mapView.mapboxMap.style?.apply {
                routeLineView.renderRouteDrawData(this, value)
            }
        }
    }
    private val onPositionChangedListener = OnIndicatorPositionChangedListener { point ->
        val result = routeLineApi.updateTraveledRouteLine(point)
        binding.mapView.mapboxMap.style?.apply {
            routeLineView.renderRouteLineUpdate(this, result)
        }
    }

    private val routeProgressObserver = RouteProgressObserver { routeProgress ->
        routeLineApi.updateWithRouteProgress(routeProgress) { result ->
            binding.mapView.mapboxMap.style?.apply {
                routeLineView.renderRouteLineUpdate(this, result)
            }
        }
        val arrowUpdate = routeArrowApi.addUpcomingManeuverArrow(routeProgress)
        binding.mapView.mapboxMap.style?.apply {
            routeArrowView.renderManeuverUpdate(this, arrowUpdate)
        }
    }

    private val locationObserver = object : LocationObserver {
        override fun onNewRawLocation(rawLocation: com.mapbox.common.location.Location) {}
        override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
            val enhancedLocation = locationMatcherResult.enhancedLocation
            navigationLocationProvider.changePosition(
                enhancedLocation,
                locationMatcherResult.keyPoints,
            )
            updateCamera(
                Point.fromLngLat(
                    enhancedLocation.longitude, enhancedLocation.latitude
                ),
                enhancedLocation.bearing
            )
        }
    }

    private val mapboxNavigation: MapboxNavigation by requireMapboxNavigation(
        onResumedObserver = object : MapboxNavigationObserver {
            @SuppressLint("MissingPermission")
            override fun onAttached(mapboxNavigation: MapboxNavigation) {
                mapboxNavigation.registerRoutesObserver(routesObserver)
                mapboxNavigation.registerRoutesPreviewObserver(routesPreviewObserver)
                mapboxNavigation.registerLocationObserver(locationObserver)
                mapboxNavigation.registerRouteProgressObserver(routeProgressObserver)
                replayProgressObserver = ReplayProgressObserver(mapboxNavigation.mapboxReplayer)
                mapboxNavigation.registerRouteProgressObserver(replayProgressObserver)
                mapboxNavigation.startReplayTripSession()
                fetchRoute()
            }

            override fun onDetached(mapboxNavigation: MapboxNavigation) {
                mapboxNavigation.unregisterRoutesObserver(routesObserver)
                mapboxNavigation.unregisterRoutesPreviewObserver(routesPreviewObserver)
                mapboxNavigation.unregisterLocationObserver(locationObserver)
                mapboxNavigation.unregisterRouteProgressObserver(routeProgressObserver)
                mapboxNavigation.unregisterRouteProgressObserver(replayProgressObserver)
                mapboxNavigation.mapboxReplayer.finish()
            }
        },
        onInitialize = this::initNavigation
    )
    override val bindingInflater: (LayoutInflater) -> MapActivityBinding
        get() = MapActivityBinding::inflate

    override fun initView() {
        binding.mapView.mapboxMap.loadStyle(NavigationStyles.NAVIGATION_DAY_STYLE) {
            routeLineView.initializeLayers(it)
        }
    }

    override fun initData() {
        preferences = SharedPreferences(this)
    }

    override fun initEvents() {
        binding.startNavigation.setOnClickListener {
            mapboxNavigation.moveRoutesFromPreviewToNavigator()
            updateRouteCalloutType(RouteCalloutType.NAVIGATION)
            sendCommandToService(Constants.ACTION_START_OR_RESUME_SERVICE)
            binding.startNavigation.isVisible = false
            binding.routeOverview.isVisible = true
            isTracking = true
        }

        binding.routeOverview.setOnClickListener {
            if (routeCalloutAdapter.options.routeCalloutType == RouteCalloutType.ROUTES_OVERVIEW) {
                updateRouteCalloutType(RouteCalloutType.NAVIGATION)
                binding.routeOverview.text = "Route overview"
            } else {
                updateRouteCalloutType(RouteCalloutType.ROUTES_OVERVIEW)
                binding.routeOverview.text = "Following mode"
            }
        }
    }

    override fun initObserve() {
        GpsTrackerService.isTracking.observe(this, Observer {
            updateTracking(it)
        })

        GpsTrackerService.pathPoints.observe(this, Observer{
            if (pathPoints.isEmpty()) {
//                val latLng = LatLng(preferences.getUserLoc()!![1], preferences.getUserLoc()!![0])
//                val
//                pathPoints.add(latLng)
            }
            pathPoints.add(it)
            moveCameraToUser()
//            fetchRoute()
            Log.e("zzzzzz","${pathPoints}")
        })
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if (!isTracking) {
            sendCommandToService(Constants.ACTION_START_OR_RESUME_SERVICE)
//            btnToggleRun.text = "Start"
//            btnFinishRun.visibility = View.VISIBLE

        } else {
            //btnToggleRun.text = "Stop"
            // menu?.getItem(0)?.isVisible = true
            //btnFinishRun.visibility = View.GONE
        }
    }

    private fun sendCommandToService(action: String) = Intent(this@MapActivity, GpsTrackerService::class.java).also {
        it.action = action
        startService(it)
    }


    private fun moveCameraToUser() {
//        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
//            map?.animateCamera(
//                CameraUpdateFactory.newLatLngZoom(
//                    pathPoints.last().last(),
//                    MAP_ZOOM
//                )
//            )
//        }
    }

    private fun updateRouteCalloutType(@RouteCalloutType.Type type: Int) {
        routeCalloutAdapter.updateOptions(
            routeCalloutAdapterOptions.toBuilder()
                .routeCalloutType(type)
                .build()
        )
    }

    private fun initNavigation() {
        MapboxNavigationApp.setup(
            NavigationOptions.Builder(this)
                .routeAlternativesOptions(
                    RouteAlternativesOptions.Builder()
                        .intervalMillis(30.seconds.inWholeMilliseconds)
                        .build()
                )
                .build()
        )
        locationComponent = binding.mapView.location.apply {
            setLocationProvider(navigationLocationProvider)
            addOnIndicatorPositionChangedListener(onPositionChangedListener)
            enabled = true
        }
        replayOriginLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        routeLineView.cancel()
        routeLineApi.cancel()
        locationComponent.removeOnIndicatorPositionChangedListener(onPositionChangedListener)
    }

    private fun fetchRoute() {
        val origin = Point.fromLngLat(105.802682, 21.024955)
        val destination = Point.fromLngLat(105.812639 , 21.025943)
        val routeCoordinates = listOf(
            origin,
            destination,
        )
        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .applyLanguageAndVoiceUnitOptions(this)
                .coordinatesList(routeCoordinates)
                .alternatives(true)
                .layersList(listOf(mapboxNavigation.getZLevel(), null))
                .build(),

            object : NavigationRouterCallback {
                override fun onRoutesReady(
                    routes: List<NavigationRoute>,
                    @RouterOrigin routerOrigin: String
                ) {
                    binding.startNavigation.isVisible = true
                    mapboxNavigation.setRoutesPreview(routes)
                }

                override fun onFailure(
                    reasons: List<RouterFailure>,
                    routeOptions: RouteOptions
                ) {
                    Log.d(LOG_TAG, "onFailure: $reasons")
                }

                override fun onCanceled(
                    routeOptions: RouteOptions,
                    @RouterOrigin routerOrigin: String
                ) {
                    Log.d(LOG_TAG, "onCanceled")
                }
            }
        )
    }

    private fun replayOriginLocation() {
        val origin = Point.fromLngLat(105.802682, 21.024955)
        val destination = Point.fromLngLat(105.812639 , 21.025943)
        val routeCoordinates = listOf(
            origin,
            destination,
        )
        with(mapboxNavigation.mapboxReplayer) {
            play()
            pushEvents(listOf(ReplayRouteMapper.mapToUpdateLocation(Date().time.toDouble(), routeCoordinates.first())))
            playFirstLocation()
            playbackSpeed(0.5)
        }
    }

    private fun updateCamera(point: Point, bearing: Double?) {
        val origin = Point.fromLngLat(105.802682, 21.024955)
        val destination = Point.fromLngLat(105.812639 , 21.025943)
        val routeCoordinates = listOf(
            origin,
            destination,
        )
        val cameraOptions = if (routeCalloutAdapter.options.routeCalloutType == RouteCalloutType.ROUTES_OVERVIEW) {
            binding.mapView.mapboxMap.cameraForCoordinates(
                listOf(point, routeCoordinates.last()),
                CameraOptions.Builder()
                    .bearing(bearing)
                    .padding(EdgeInsets(100.0, 100.0, 100.0, 100.0))
                    .build(),
                null,
                null,
                null,
            )
        } else {
            CameraOptions.Builder()
                .center(point)
                .bearing(bearing)
                .pitch(45.0)
                .zoom(20.0)
                .padding(EdgeInsets(1000.0, 0.0, 0.0, 0.0))
                .build()
        }
        val mapAnimationOptionsBuilder = MapAnimationOptions.Builder()
        binding.mapView.camera.easeTo(
            cameraOptions,
            mapAnimationOptionsBuilder.build(),
        )
    }

    private fun reorderRoutes(clickedRoute: NavigationRoute) {
        if (clickedRoute != routeLineApi.getPrimaryNavigationRoute()) {
            if (mapboxNavigation.getRoutesPreview() == null) {
                val reOrderedRoutes = mapboxNavigation.getNavigationRoutes()
                    .filter { clickedRoute.id != it.id }
                    .toMutableList()
                    .also { list ->
                        list.add(0, clickedRoute)
                    }
                mapboxNavigation.setNavigationRoutes(reOrderedRoutes)
            } else {
                mapboxNavigation.changeRoutesPreviewPrimaryRoute(clickedRoute)
            }
        }
    }

    private companion object {
        val LOG_TAG: String = MapActivity::class.java.simpleName
    }
}