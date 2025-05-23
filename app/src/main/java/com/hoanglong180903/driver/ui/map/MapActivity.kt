package com.hoanglong180903.driver.ui.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.hoanglong180903.driver.databinding.MapActivityBinding
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapboxDelicateApi
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
class MapActivity : AppCompatActivity() {

    private val routeCoordinates = listOf(
        Point.fromLngLat(105.802682, 21.024955),
        Point.fromLngLat(105.812639 , 21.025943),
    )

    private lateinit var locationComponent: LocationComponentPlugin


    private lateinit var replayProgressObserver: ReplayProgressObserver

    private val viewBinding: MapActivityBinding by lazy {
        MapActivityBinding.inflate(layoutInflater)
    }

    private val navigationLocationProvider by lazy {
        /* route line */
        Log.e("LogMap","navigationLocationProvider - 3")
        NavigationLocationProvider()
    }

    private val routeLineView by lazy {
        MapboxRouteLineView(routeLineViewOptions).also {
            /* route line */
            Log.e("LogMap","routeLineView MapboxRouteLineView")
            it.setCalloutAdapter(viewBinding.mapView.viewAnnotationManager, routeCalloutAdapter)
        }
    }

    private val routeLineViewOptions: MapboxRouteLineViewOptions by lazy {
        /* route line */
        Log.e("LogMap","routeLineViewOptions")
        MapboxRouteLineViewOptions.Builder(this)
            .routeLineColorResources(RouteLineColorResources.Builder().build())
            .routeLineBelowLayerId("road-label-navigation")
            .build()
    }

    private val routeLineApiOptions: MapboxRouteLineApiOptions by lazy {
        /* route line */

        Log.e("LogMap","routeLineApiOptions")
        MapboxRouteLineApiOptions.Builder()
            .vanishingRouteLineEnabled(true)
            .isRouteCalloutsEnabled(true)
            .build()
    }

    private val routeLineApi: MapboxRouteLineApi by lazy {
        /* route line */
        Log.e("LogMap","routeLineApi")
        MapboxRouteLineApi(routeLineApiOptions)
    }
    private val routeArrowApi: MapboxRouteArrowApi by lazy {
        Log.e("LogMap","routeArrowApi")
        /* nav */

        MapboxRouteArrowApi()
    }
    private val routeArrowOptions by lazy {
        Log.e("LogMap","routeArrowOptions")
        /* nav */

        RouteArrowOptions.Builder(this)
            .withAboveLayerId(TOP_LEVEL_ROUTE_LINE_LAYER_ID)
            .build()
    }
    private val routeArrowView: MapboxRouteArrowView by lazy {
        Log.e("LogMap","routeArrowView")
        /* nav */

        MapboxRouteArrowView(routeArrowOptions)
    }
    private val routeCalloutAdapter: DefaultRouteCalloutAdapter by lazy {
        /* route line */

        Log.e("LogMap","routeCalloutAdapter")
        DefaultRouteCalloutAdapter(this, routeCalloutAdapterOptions) { data ->
            reorderRoutes(data.route)
        }
    }
    private val routeCalloutAdapterOptions: DefaultRouteCalloutAdapterOptions by lazy {
        /* route line */

        Log.e("LogMap","routeCalloutAdapterOptions")
        DefaultRouteCalloutAdapterOptions.Builder()
            .similarDurationDelta(1.minutes)
            .routeCalloutType(RouteCalloutType.ROUTES_OVERVIEW)
            .build()
    }
    private val routesObserver: RoutesObserver = RoutesObserver { routeUpdateResult ->
        /* nav */

        Log.e("LogMap","routesObserver")
        updateRoutes(
            routeUpdateResult.navigationRoutes,
            mapboxNavigation.getAlternativeMetadataFor(routeUpdateResult.navigationRoutes)
        )
    }
    private val routesPreviewObserver: RoutesPreviewObserver = RoutesPreviewObserver { update ->
        /* route line */
        /* nav */
        Log.e("LogMap","routesPreviewObserver")
        val preview = update.routesPreview ?: return@RoutesPreviewObserver

        updateRoutes(preview.routesList, preview.alternativesMetadata)
    }

    private fun updateRoutes(routesList: List<NavigationRoute>, alternativesMetadata: List<AlternativeRouteMetadata>) {
        routeLineApi.setNavigationRoutes(routesList, alternativesMetadata) { value ->
            /* route line */
            Log.e("LogMap","updateRoutes  routeLineApi.setNavigationRoutes")
            /* nav */
            viewBinding.mapView.mapboxMap.style?.apply {
                /* route line */
                routeLineView.renderRouteDrawData(this, value)
                Log.e("LogMap","updateRoutes  routeLineView.renderRouteDrawData")
                /* nav */

            }
        }
    }
    private val onPositionChangedListener = OnIndicatorPositionChangedListener { point ->
//        Log.e("LogMap","onPositionChangedListener")
        val result = routeLineApi.updateTraveledRouteLine(point)
        viewBinding.mapView.mapboxMap.style?.apply {
            routeLineView.renderRouteLineUpdate(this, result)
            /* route line */
//            Log.e("LogMap","onPositionChangedListener routeLineView.renderRouteLineUpdate")
        }
    }

    private val routeProgressObserver = RouteProgressObserver { routeProgress ->
        Log.e("LogMap","routeProgressObserver")
        /* nav */

        routeLineApi.updateWithRouteProgress(routeProgress) { result ->
            viewBinding.mapView.mapboxMap.style?.apply {
                routeLineView.renderRouteLineUpdate(this, result)
                Log.e("LogMap","routeProgressObserver routeLineView.renderRouteLineUpdate")

            }
        }
        val arrowUpdate = routeArrowApi.addUpcomingManeuverArrow(routeProgress)
        viewBinding.mapView.mapboxMap.style?.apply {
            routeArrowView.renderManeuverUpdate(this, arrowUpdate)
            Log.e("LogMap","routeProgressObserver routeLineView.renderManeuverUpdate")
            /* nav */

        }
    }

    private val locationObserver = object : LocationObserver {
        override fun onNewRawLocation(rawLocation: com.mapbox.common.location.Location) {}
        override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
            /* route line */
            Log.e("LogMap","locationObserver onNewLocationMatcherResult")
            /* nav */

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
                /* route line */
                Log.e("LogMap","mapboxNavigation onAttached - 5")
            }

            override fun onDetached(mapboxNavigation: MapboxNavigation) {
                mapboxNavigation.unregisterRoutesObserver(routesObserver)
                mapboxNavigation.unregisterRoutesPreviewObserver(routesPreviewObserver)
                mapboxNavigation.unregisterLocationObserver(locationObserver)
                mapboxNavigation.unregisterRouteProgressObserver(routeProgressObserver)
                mapboxNavigation.unregisterRouteProgressObserver(replayProgressObserver)
                mapboxNavigation.mapboxReplayer.finish()
                Log.e("LogMap","mapboxNavigation onDetached")

            }
        },
        onInitialize = this::initNavigation
    )

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.mapView.mapboxMap.loadStyle(NavigationStyles.NAVIGATION_DAY_STYLE) {
            routeLineView.initializeLayers(it)
            Log.e("LogMap","routeLineView.initializeLayers")
        }

        viewBinding.startNavigation.setOnClickListener {
            mapboxNavigation.moveRoutesFromPreviewToNavigator()
            updateRouteCalloutType(RouteCalloutType.NAVIGATION)
            /* nav */

            Log.e("LogMap","startNavigation")

        }
    }

    private fun updateRouteCalloutType(@RouteCalloutType.Type type: Int) {
        /* nav */
        Log.e("LogMap","updateRouteCalloutType")
        routeCalloutAdapter.updateOptions(
            routeCalloutAdapterOptions.toBuilder()
                .routeCalloutType(type)
                .build()
        )
    }

    private fun initNavigation() {
        /* route line */
        Log.e("LogMap","initNavigation - 1")
        MapboxNavigationApp.setup(
            NavigationOptions.Builder(this)
                .routeAlternativesOptions(
                    RouteAlternativesOptions.Builder()
                        .intervalMillis(30.seconds.inWholeMilliseconds)
                        .build()
                )
                .build()
        )

        locationComponent = viewBinding.mapView.location.apply {
            /* route line */
            Log.e("LogMap","locationComponent - 2")
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
        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .applyLanguageAndVoiceUnitOptions(this)
                .coordinatesList(routeCoordinates)
                .alternatives(true)
                .layersList(listOf(mapboxNavigation.getZLevel(), null))
                .build(),

            object : NavigationRouterCallback {
                override fun onRoutesReady(routes: List<NavigationRoute>, @RouterOrigin routerOrigin: String) {
                    viewBinding.startNavigation.isVisible = true
                    mapboxNavigation.setRoutesPreview(routes)
                    /* route line */
                    Log.e("LogMap","fetchRoute")
                }

                override fun onFailure(reasons: List<RouterFailure>, routeOptions: RouteOptions) {}

                override fun onCanceled(routeOptions: RouteOptions, @RouterOrigin routerOrigin: String) { }
            }
        )
    }
    //hiện vị trí ban đầu
    private fun replayOriginLocation() {
        /* route line */
        with(mapboxNavigation.mapboxReplayer) {
            play()
            pushEvents(listOf(ReplayRouteMapper.mapToUpdateLocation(Date().time.toDouble(), routeCoordinates.first())))
            playFirstLocation()
            playbackSpeed(1.0)
            Log.e("LogMap","replayOriginLocation - 4")
        }
    }

    @OptIn(MapboxDelicateApi::class)
    private fun updateCamera(point: Point, bearing: Double?) {
        val cameraOptions = if (routeCalloutAdapter.options.routeCalloutType == RouteCalloutType.ROUTES_OVERVIEW) {
            viewBinding.mapView.mapboxMap.cameraForCoordinates(
                listOf(point, routeCoordinates.last()),
                CameraOptions.Builder()
                    .bearing(0.0)
                    .padding(EdgeInsets(100.0, 100.0, 800.0, 100.0))
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
                .zoom(17.0)
                .padding(EdgeInsets(300.0, 0.0, 0.0, 0.0))
                .build()
        }
        val mapAnimationOptionsBuilder = MapAnimationOptions.Builder()
        viewBinding.mapView.camera.easeTo(
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
                Log.e("LogMap","reorderRoutes setNavigationRoutes")
            } else {
                mapboxNavigation.changeRoutesPreviewPrimaryRoute(clickedRoute)
                Log.e("LogMap","reorderRoutes changeRoutesPreviewPrimaryRoute")
            }
        }
    }
}