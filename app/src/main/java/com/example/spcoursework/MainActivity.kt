package com.example.spcoursework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.spcoursework.databinding.ActivityMainBinding
import com.example.spcoursework.domain.db.AutoRepairDB
import com.example.spcoursework.domain.network.ApiService
import com.example.spcoursework.domain.network.SessionManager
import com.example.spcoursework.entities.Client
import com.example.spcoursework.entities.Employee
import com.example.spcoursework.entities.EmployeeRoles
import com.example.spcoursework.entities.Request
import com.example.spcoursework.entities.RequestStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not be null")

    private lateinit var conf: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SessionManager.initialize(applicationContext)
        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        conf = AppBarConfiguration(
            setOf(
                R.id.holderFragment,
                R.id.createRequestFragment,
                R.id.createEmployeeFragment,
            ), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, conf)
        binding.navView.setupWithNavController(navController)

        binding.navView.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            SessionManager.logout()
            binding.drawerLayout.closeDrawers()
            navController.popBackStack()
            return@setOnMenuItemClickListener false
        }



        val dao = AutoRepairDB.getInstance(applicationContext).getDao()

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                //AutoRepairDB.getInstance(applicationContext).clearAllTables()
                //dao.insertEmployee(employerAdmin)
            }
//            val carNumber = "123asd"
//            dao.insertRequestWithClientIdByCarNumber(
//                carNumber,
//                Request(
//                    id = 0,
//                    clientId = 0,//get client id by car's number
//                    carNumber = carNumber,
//                    workerId = null,
//                    problemDescription = "пупупум",
//                    status = RequestStatus.PENDING
//                )
//            )
//
//            dao.insertClient(
//                Client(
//                    0, name = "Anton",
//                    phoneNumber = "8900",
//                    carNumber = "123asd"
//                )
//            )
//
//            dao.insertEmployee(
//                Employee(
//                    name = "Sergey",
//                    password = "qwe",
//                    phoneNumber = "8800553535",
//                    role = EmployeeRoles.WORKER
//                )
//            )

//            dao.insertRequest(
//                Request(
//                    id = 0,
//                    clientId = 1,//get client id by car's number
//                    carNumber = "123asd",
//                    workerId = UUID.fromString("25fd4810-3240-4cf8-8935-fb74525f79ef"),
//                    problemDescription = "",
//                    status = RequestStatus.WORKING
//                )
//            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(conf) || super.onSupportNavigateUp()
    }


    val employerAdmin = Employee(
        name = "ADMIN",
        password = "qwe",
        phoneNumber = "111",
        role = EmployeeRoles.ADMIN
    )
}

