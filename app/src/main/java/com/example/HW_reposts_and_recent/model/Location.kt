package com.example.HW_reposts_and_recent.model

class Location(val lat: Double, val lng: Double)

infix fun Double.x(that: Double) = Location(this, that)
