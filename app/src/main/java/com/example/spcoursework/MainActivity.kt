package com.example.spcoursework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.spcoursework.databinding.ActivityMainBinding
import com.example.spcoursework.domain.db.AutoRepairDB
import com.example.spcoursework.domain.network.SessionManager
import com.example.spcoursework.entities.Employee
import com.example.spcoursework.entities.EmployeeRoles
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
            navController.navigate(R.id.loginFragment)
            return@setOnMenuItemClickListener false
        }

        //TODO разделение по ролям, доделать работника
        binding.navView.menu.findItem(R.id.createRequestFragment).setVisible(false)
        binding.navView.menu.findItem(R.id.createRequestFragment)


        val dao = AutoRepairDB.getInstance(applicationContext).getDao()

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                //AutoRepairDB.getInstance(applicationContext).clearAllTables()
                //dao.insertEmployee(employerAdmin)
            }
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

