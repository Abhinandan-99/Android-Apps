package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKE=2.00
private const val PRICE_FOR_SAME_DAY=3.00

class OrderViewModel : ViewModel() {

    private val _quantity=MutableLiveData<Int>(0)
    val quantity:LiveData<Int>
    get() = _quantity

    private val _flavor=MutableLiveData<String>("")
    val flavor:LiveData<String>
    get() = _flavor

    private val _date=MutableLiveData<String>("")
    val date:LiveData<String>
    get() = _date

    private val _price=MutableLiveData<Double>(0.0)
    val price:LiveData<String>
    get() = Transformations.map(_price)
    {
        NumberFormat.getCurrencyInstance().format(it)
    }


    fun setQuantity(numberCupcakes:Int)
    {
        _quantity.value=numberCupcakes
        updatePrice()
    }

    fun setFlavour(desiredFlavour:String)
    {
        _flavor.value=desiredFlavour
    }

    fun setDate(pickupDate:String)
    {
        _date.value=pickupDate
        updatePrice()
    }

    fun hasNoFlavorSet():Boolean{
        return _flavor.value.isNullOrEmpty()
    }

    private fun getPickupOptions(): List<String>
    {
        val options= mutableListOf<String>()
        val formatter=SimpleDateFormat("E MM d", Locale.getDefault())
        val calender=Calendar.getInstance()
        repeat(4)
        {
            options.add(formatter.format(calender.time))
            calender.add(Calendar.DATE,1)
        }
        return options
    }


    val dateOptions=getPickupOptions()

    private fun updatePrice()
    {
        var calculatedPrice=(_quantity.value?:0)* PRICE_PER_CUPCAKE

        if(_date.value== dateOptions[0])
        {
            calculatedPrice+= PRICE_FOR_SAME_DAY
        }
        _price.value=calculatedPrice
    }

    fun resetOrder()
    {
        _quantity.value=0
        _flavor.value=""
        _date.value=""
        _price.value=0.0
    }

}