package com.hoanglong180903.driver.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.hoanglong180903.driver.common.base.BaseActivity
import com.hoanglong180903.driver.databinding.MapActivityBinding
import com.hoanglong180903.driver.utils.PopupUtils
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.common.location.Location
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.ExperimentalPreviewMapboxNavigationAPI
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.extensions.applyLanguageAndVoiceUnitOptions
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.base.route.NavigationRoute
import com.mapbox.navigation.base.route.NavigationRouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.route.RouterOrigin
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp
import com.mapbox.navigation.core.lifecycle.MapboxNavigationObserver
import com.mapbox.navigation.core.lifecycle.requireMapboxNavigation
import com.mapbox.navigation.core.replay.route.ReplayProgressObserver
import com.mapbox.navigation.core.replay.route.ReplayRouteMapper
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineApiOptions
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineViewOptions

class NavigationMapActivity : BaseActivity<MapActivityBinding>() {
    private lateinit var viewportDataSource: MapboxNavigationViewportDataSource
    private lateinit var navigationCamera: NavigationCamera
    private lateinit var routeLineApi: MapboxRouteLineApi
    private lateinit var routeLineView: MapboxRouteLineView
    private lateinit var replayProgressObserver: ReplayProgressObserver
    private val navigationLocationProvider = NavigationLocationProvider()
    private val replayRouteMapper = ReplayRouteMapper()
    override val bindingInflater: (LayoutInflater) -> MapActivityBinding get() = MapActivityBinding::inflate

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                permissions ->
            when {
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                    initializeMapComponents()
                }
                else -> {
                    PopupUtils.showToast(this,"Quyền vị trí chưa được cấp. Vui lòng cấp quyền trong cài đặt.")
                }
            }
        }
    override fun initView() {
    }

    override fun initData() {
        if (
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            initializeMapComponents()
        } else {
            locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    override fun initEvents() {
        binding.startNavigation.setOnClickListener {
            startNavigation()
        }
    }

    override fun initObserve() {
    }

    private fun initializeMapComponents() {
        binding.mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(105.802682, 21.024955))
                .zoom(14.0)
                .build()
        )
        binding.mapView.location.apply {
            setLocationProvider(navigationLocationProvider)
            locationPuck = LocationPuck2D()
            enabled = true
        }

        viewportDataSource = MapboxNavigationViewportDataSource(binding.mapView.mapboxMap)
        val pixelDensity = this.resources.displayMetrics.density
        viewportDataSource.followingPadding =
            EdgeInsets(
                180.0 * pixelDensity,
                40.0 * pixelDensity,
                150.0 * pixelDensity,
                40.0 * pixelDensity
            )

        navigationCamera = NavigationCamera(binding.mapView.mapboxMap, binding.mapView.camera, viewportDataSource)
        routeLineApi = MapboxRouteLineApi(MapboxRouteLineApiOptions.Builder().build())
        routeLineView = MapboxRouteLineView(MapboxRouteLineViewOptions.Builder(this).build())
    }

    private val routesObserver = RoutesObserver { routeUpdateResult ->
        if (::routeLineApi.isInitialized && ::routeLineView.isInitialized && ::viewportDataSource.isInitialized) {
            routeLineApi.setNavigationRoutes(routeUpdateResult.navigationRoutes) { value ->
                binding.mapView.mapboxMap.style?.apply { routeLineView.renderRouteDrawData(this, value) }
            }
            viewportDataSource.onRouteChanged(routeUpdateResult.navigationRoutes.first())
            viewportDataSource.evaluate()
            navigationCamera.requestNavigationCameraToOverview()
        }
    }


    private val locationObserver =
        object : LocationObserver {
            override fun onNewRawLocation(rawLocation: Location) {}

            override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
                val enhancedLocation = locationMatcherResult.enhancedLocation
                if (::navigationCamera.isInitialized && ::viewportDataSource.isInitialized) {
                    navigationLocationProvider.changePosition(
                        location = enhancedLocation,
                        keyPoints = locationMatcherResult.keyPoints,
                    )
                    viewportDataSource.onLocationChanged(enhancedLocation)
                    viewportDataSource.evaluate()
                    navigationCamera.requestNavigationCameraToFollowing()
                }
            }
        }

    @OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
    private val mapboxNavigation: MapboxNavigation by
    requireMapboxNavigation(
        onResumedObserver =
        object : MapboxNavigationObserver {
            @SuppressLint("MissingPermission")
            override fun onAttached(mapboxNavigation: MapboxNavigation) {
                mapboxNavigation.registerRoutesObserver(routesObserver)
                mapboxNavigation.registerLocationObserver(locationObserver)
                replayProgressObserver = ReplayProgressObserver(mapboxNavigation.mapboxReplayer)
                fetchRoute()
            }
            override fun onDetached(mapboxNavigation: MapboxNavigation) {
                mapboxNavigation.registerRoutesObserver(routesObserver)
                mapboxNavigation.registerLocationObserver(locationObserver)
            }
        },
        onInitialize = this::initNavigation
    )

    @OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
    private fun fetchRoute() {
        val routeCoordinates = listOf(
            Point.fromLngLat(105.802682, 21.024955),
            Point.fromLngLat(105.812639 , 21.02594),
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
                    Log.d("zzzzzz", "onFailure: $reasons")
                }

                override fun onCanceled(
                    routeOptions: RouteOptions,
                    @RouterOrigin routerOrigin: String
                ) {
                    Log.d("zzzzzzz", "onCanceled")
                }
            }
        )
    }

    private fun initNavigation() {
        MapboxNavigationApp.setup(NavigationOptions.Builder(this).build())
        binding.mapView.location.apply {
            setLocationProvider(navigationLocationProvider)
            this.locationPuck = createDefault2DPuck()
            enabled = true
        }
        val origin = Point.fromLngLat(105.802682, 21.024955)
        val toLocation = intent.getSerializableExtra("toLocation") as? ArrayList<Double>
        val destination = Point.fromLngLat(toLocation?.get(1) ?: 0.0,toLocation?.get(0) ?: 0.0)
        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .coordinatesList(listOf(origin, destination))
                .layersList(listOf(mapboxNavigation.getZLevel(), null))
                .build(),
            object : NavigationRouterCallback {
                override fun onCanceled(routeOptions: RouteOptions, routerOrigin: String) {}

                override fun onFailure(reasons: List<RouterFailure>, routeOptions: RouteOptions) {}

                override fun onRoutesReady(routes: List<NavigationRoute>, routerOrigin: String) {
                    mapboxNavigation.setNavigationRoutes(routes)
//                    val replayData =
//                        replayRouteMapper.mapDirectionsRouteGeometry(routes.first().directionsRoute)
//                    mapboxNavigation.mapboxReplayer.pushEvents(replayData)
//                    mapboxNavigation.mapboxReplayer.seekTo(replayData[0])
//                    mapboxNavigation.mapboxReplayer.play()
                }
            }
        )
    }

    @OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
    private fun startNavigation() {
        val origin = Point.fromLngLat(105.802682, 21.024955)
        val destination = Point.fromLngLat(105.812639 , 21.025943)
        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .coordinatesList(listOf(origin, destination))
                .layersList(listOf(mapboxNavigation.getZLevel(), null))
                .build(),
            object : NavigationRouterCallback {
                override fun onCanceled(routeOptions: RouteOptions, routerOrigin: String) {}

                override fun onFailure(reasons: List<RouterFailure>, routeOptions: RouteOptions) {}

                override fun onRoutesReady(routes: List<NavigationRoute>, routerOrigin: String) {
                    mapboxNavigation.setNavigationRoutes(routes)

                    val replayData =
                        replayRouteMapper.mapDirectionsRouteGeometry(routes.first().directionsRoute)
                    mapboxNavigation.mapboxReplayer.pushEvents(replayData)
                    mapboxNavigation.mapboxReplayer.seekTo(replayData[0])
                    mapboxNavigation.mapboxReplayer.play()
                    mapboxNavigation.startReplayTripSession()
                }
            }
        )
    }

}