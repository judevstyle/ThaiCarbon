package com.taitos.nup.common

import androidx.lifecycle.LiveData


abstract class LiveEvent<T> : LiveData<Event<T>> {

    constructor() : super()
    constructor(value: T) : super(Event(value))

    val eventValue: T? get() = super.getValue()?.peekContent()

    protected open fun setEventValue(value: T) {
        super.setValue(Event(value))
    }

    protected open fun postEventValue(value: T) {
        super.postValue(Event(value))
    }

}

class MutableLiveEvent<T> : LiveEvent<T> {
    constructor() : super()
    constructor(value: T) : super(value)

    public override fun setEventValue(value: T) {
        super.setEventValue(value)
    }

    public override fun postEventValue(value: T) {
        super.postEventValue(value)
    }
}