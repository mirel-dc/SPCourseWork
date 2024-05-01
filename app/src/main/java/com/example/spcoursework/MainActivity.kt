package com.example.spcoursework

import android.os.Bundle
import android.util.Log
import android.widget.TextView
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
import kotlinx.coroutines.launch

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

        val headerRole = binding.navView.getHeaderView(0)
            .findViewById<TextView>(R.id.tvEmployeeRole)
        val headerName = binding.navView.getHeaderView(0)
            .findViewById<TextView>(R.id.tvEmployeeName)


        SessionManager.roleLiveData.observe(this) { role ->
            Log.d(TAG, "ROLE CHANGED $role")
            when (role) {
                EmployeeRoles.WORKER -> {
                    setCreatingVisible(false)
                    headerRole.text = getString(EmployeeRoles.WORKER.resId)
                }

                EmployeeRoles.ADMIN -> {
                    headerRole.text = getString(EmployeeRoles.ADMIN.resId)
                    setCreatingVisible(true)
                }

                null -> {
                    Log.e(TAG, "Role is null")
                }
            }
        }

        SessionManager.employeeNameLD.observe(this) { name ->
            headerName.text = name
        }

        lifecycleScope.launch {
            val dao = AutoRepairDB.getInstance(this@MainActivity).getDao()
            dao.insertEmployee(employerAdmin)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(conf) || super.onSupportNavigateUp()
    }

    private fun setCreatingVisible(flag: Boolean) {
        binding.navView.menu.findItem(R.id.createRequestFragment).setVisible(flag)
        binding.navView.menu.findItem(R.id.createEmployeeFragment).setVisible(flag)
    }
}

//Init user
val employerAdmin = Employee(
    name = "AdminUser",
    password = "admin",
    phoneNumber = "111",
    role = EmployeeRoles.ADMIN
)
