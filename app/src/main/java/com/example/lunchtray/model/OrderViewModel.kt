package com.example.lunchtray.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.lunchtray.data.DataSource
import java.text.NumberFormat

class OrderViewModel : ViewModel() {

    // Map of menu items
    val menuItems = DataSource.menuItems

    // Default values for item prices
    private var previousEntreePrice = 0.0
    private var previousSidePrice = 0.0
    private var previousAccompanimentPrice = 0.0

    // Default tax rate
    private val taxRate = 0.08

    // Entree for the order
    private val _entree = MutableLiveData<MenuItem?>()
    val entree: LiveData<MenuItem?> = _entree

    // Side for the order
    private val _side = MutableLiveData<MenuItem?>()
    val side: LiveData<MenuItem?> = _side

    // Accompaniment for the order.
    private val _accompaniment = MutableLiveData<MenuItem?>()
    val accompaniment: LiveData<MenuItem?> = _accompaniment

    // Subtotal for the order
    private val _subtotal = MutableLiveData<Double>()
    val subtotal: LiveData<String> = Transformations.map(_subtotal) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    // Total cost of the order
    private val _total = MutableLiveData<Double>()
    val total: LiveData<String> = Transformations.map(_total) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    // Tax for the order
    private val _tax = MutableLiveData<Double>()
    val tax: LiveData<String> = Transformations.map(_tax) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    /**
     * Set the entree for the order.
     */
    fun setEntree(entree: String) {
        if(_entree.value != null) {
            previousEntreePrice = _entree.value!!.price
        }
        if(_subtotal.value != null) {
            _subtotal.value = _subtotal.value!! - previousEntreePrice
        }
        _entree.value = menuItems[entree]
        updateSubtotal(_entree.value!!.price)

    }

    /**
     * Set the side for the order.
     */
    fun setSide(side: String) {
        if(_side.value != null) {
            previousSidePrice = _side.value!!.price
        }
        if(_subtotal.value != null) {
            _subtotal.value = _subtotal.value!! - previousSidePrice
        }
        _side.value = menuItems[side]
        updateSubtotal(_side.value!!.price)
    }

    /**
     * Set the accompaniment for the order.
     */
    fun setAccompaniment(accompaniment: String) {
        if(_accompaniment.value != null) {
            previousAccompanimentPrice = _accompaniment.value!!.price
        }
        if(_accompaniment.value != null) {
            _subtotal.value = _subtotal.value!! - previousAccompanimentPrice
        }
        _accompaniment.value = menuItems[accompaniment]
        updateSubtotal(_accompaniment.value!!.price)
    }

    /**
     * Update subtotal value.
     */
    private fun updateSubtotal(itemPrice: Double) {
        if(_subtotal.value != null) {
            _subtotal.value = _subtotal.value!! + itemPrice
        } else {
            _subtotal.value = itemPrice
        }
        calculateTaxAndTotal()
    }

    /**
     * Calculate tax and update total.
     */
    fun calculateTaxAndTotal() {
        _tax.value = taxRate * _subtotal.value!!
        _total.value = _tax.value!! + _subtotal.value!!
    }

    init {
        resetOrder()
    }

    /**
     * Reset all values pertaining to the order.
     */
    fun resetOrder() {
        _entree.value = null
        _side.value = null
        _accompaniment.value = null
        _subtotal.value = 0.0
        _tax.value = 0.0
        _total.value = 0.0
    }
}
