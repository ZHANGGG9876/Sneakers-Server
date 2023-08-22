package com.sneakers_shop_server.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.sneakers_shop_server.model.OrderModel
import com.sneakers_shop_server.model.SneakerModel

class SneakerViewModel : ViewModel() {

    //ViewModel Sneakers
    private val db_sneakers = Firebase.database.getReference("Sneaker")
    private val _sneaker = MutableLiveData<SneakerModel>()
    val sneaker: LiveData<SneakerModel> = _sneaker
    private val _sneakers = MutableLiveData<List<SneakerModel>>()
    val sneakers: LiveData<List<SneakerModel>> = _sneakers

    //ViewModel orders
    private val db_orders = Firebase.database.reference.child("User")
    private val _order = MutableLiveData<OrderModel>()
    val order: LiveData<OrderModel> = _order
    private val _orders = MutableLiveData<List<OrderModel>>()
    val orders: LiveData<List<OrderModel>> = _orders

    //ViewModel orders shipped
    private val db_orders_shipped = Firebase.database.reference.child("User")
    private val _order_shipped = MutableLiveData<OrderModel>()
    val order_shipped: LiveData<OrderModel> = _order_shipped
    private val _orders_shipped = MutableLiveData<List<OrderModel>>()
    val orders_shipped: LiveData<List<OrderModel>> = _orders_shipped

    init {
        loadSneakers(_sneakers)
        loadOrders(_orders)
        loadOrdersShipped(_orders_shipped)
    }

    fun getSneakers() {
        loadSneakers(_sneakers)
    }
    fun getOrders() {
        loadOrders(_orders)
    }

    // Recoger los sneakers
    private fun loadSneakers(sneakers: MutableLiveData<List<SneakerModel>>) {
        db_sneakers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val _sneakers: List<SneakerModel> = dataSnapshot.children.map {
                        it.getValue(SneakerModel::class.java)!!
                    }
                    sneakers.value = _sneakers
                    Log.i("sneakersFira", sneakers.value.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // Recoger todos los requests
    private fun loadOrders(orders: MutableLiveData<List<OrderModel>>) {
        db_orders.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val allOrders = mutableListOf<OrderModel>()
                for (userSnapshot in dataSnapshot.children) {
                    val ordersSnapshot = userSnapshot.child("orders")
                    for (orderSnapshot in ordersSnapshot.children) {
                        val order = orderSnapshot.getValue(OrderModel::class.java)
                        if (order != null && order.status.toString() == "PROCESSING") {
                            allOrders.add(order)
                        }
                    }
                }
                orders.value = allOrders
                Log.i("orders", orders.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("loadOrders", "Error fetching orders", error.toException())
            }
        })
    }

    // Recoger todos los pedidos enviados
    private fun loadOrdersShipped(orders: MutableLiveData<List<OrderModel>>) {
        db_orders_shipped.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val allOrdersShipped = mutableListOf<OrderModel>()
                for (userSnapshot in dataSnapshot.children) {
                    val ordersSnapshot = userSnapshot.child("orders")
                    for (orderSnapshot in ordersSnapshot.children) {
                        val order = orderSnapshot.getValue(OrderModel::class.java)
                        if (order != null && order.status.toString() != "PROCESSING") {
                            allOrdersShipped.add(order)
                        }
                    }
                }
                orders.value = allOrdersShipped
                Log.i("ordersshipped", orders.value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("loadOrders", "Error fetching orders", error.toException())
            }
        })
    }

    fun onSneakerClicked(sneaker: SneakerModel) {
        _sneaker.value = sneaker
    }

    fun onOrderClicked(order: OrderModel) {
        _order.value = order
    }

    fun onOrderShipedClicked(order_shipped: OrderModel) {
        _order_shipped.value = order_shipped
    }

    //Update status order
    fun updateOrder(order: OrderModel) {
        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        databaseRef.child("User").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val ordersSnapshot = userSnapshot.child("orders")
                    for (orderSnapshot in ordersSnapshot.children) {
                        val db_order = orderSnapshot.getValue(OrderModel::class.java)
                        if (db_order?.id == order.id) {
                            val orderRef = orderSnapshot.ref
                            orderRef.setValue(order)
                            return
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(
                    "updateOrder",
                    "Error al actualizar el pedido con id ${order.id}",
                    databaseError.toException()
                )
            }
        })
    }

    fun deteleOrder(order: OrderModel) {
        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        databaseRef.child("User").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val ordersSnapshot = userSnapshot.child("orders")
                    for (orderSnapshot in ordersSnapshot.children) {
                        val db_order = orderSnapshot.getValue(OrderModel::class.java)
                        if (db_order?.id == order.id) {
                            val orderRef = orderSnapshot.ref
                            orderRef.removeValue()
                            return
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(
                    "updateOrder",
                    "Error al actualizar el pedido con id ${order.id}",
                    databaseError.toException()
                )
            }
        })
    }

    fun filterSneakersByName(text: String) {
        _sneakers.value =
            _sneakers.value?.filter { it.name?.lowercase()?.contains(text.lowercase()) ?: false }
    }

    fun filterOrdersByName(text: String) {
        _orders.value =
            _orders.value?.filter { it.userName?.lowercase()?.contains(text.lowercase()) ?: false }
    }

}

