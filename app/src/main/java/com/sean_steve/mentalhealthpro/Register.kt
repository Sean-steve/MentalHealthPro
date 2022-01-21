package com.sean_steve.mentalhealthpro

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Register.newInstance] factory method to
 * create an instance of this fragment.
 */
class Register : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var registeremail : EditText
    private lateinit var newpassword : EditText
    private lateinit var confirmpassword : EditText
    private lateinit var registerbtn : Button
    private lateinit var signintext : TextView
    companion object {
        const val TAG = "LoginFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
            //param2 = it.getString(ARG_PARAM2)
            // Initialize Firebase Auth
            auth = Firebase.auth
        }
    }
//    override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        if(currentUser != null){
//        }
//    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View=inflater.inflate(R.layout.register, container, false)
        registerbtn=view.findViewById(R.id.registerbutton)
        registeremail= view.findViewById(R.id.registeremail)
        newpassword= view.findViewById(R.id.newpassword)
        confirmpassword = view.findViewById(R.id.confirmpassword)


        registerbtn.setOnClickListener{
           // createAccount(registeremail.text.toString(),newpassword.text.toString())
           validateEmptyForm()
            //launchSignInFlow()
        }
        val signinText:TextView=view.findViewById(R.id.signintext)
        signinText.setOnClickListener(){
            val fragment=Login()
            val fragmentManager=activity?.supportFragmentManager
            val transaction=fragmentManager?.beginTransaction()
            transaction?.replace(R.id.nav_host_fragment, fragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
        return view
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    activity?.let {
                        val intent = Intent(it, NavDrawer::class.java)
                        it.startActivity(intent)}
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }
    private fun updateUI(user: FirebaseUser?) {

    }
    private fun firebaseAuthenticate(){
       // txtLogin.isEnabled = false
       // txtLogin.alpha = 0.5f
        auth.createUserWithEmailAndPassword(registeremail.text.toString(), newpassword.text.toString()).addOnCompleteListener {
                task ->
            if (task.isSuccessful){
                activity?.let {
                        val intent = Intent(it, NavDrawer::class.java)
                        it.startActivity(intent)}

            }else{
                //txtLogin.isEnabled = true
                //txtLogin.alpha = 1.0f
                Toast.makeText(context,task.exception?.message, Toast.LENGTH_SHORT).show()

            }
        }

    }

    private fun validateEmptyForm() {
        when{
            TextUtils.isEmpty(registeremail.text.toString().trim())->{
                registeremail.setError("Please enter your email")
            }
            TextUtils.isEmpty(newpassword.text.toString().trim())->{
                newpassword.setError("Please enter your email")
            }
            TextUtils.isEmpty(confirmpassword.text.toString().trim())->{
                confirmpassword.setError("Please enter your email")
            }
            registeremail.text.toString().trim().isNotEmpty() &&
                    newpassword.text.toString().trim().isNotEmpty() &&
                    confirmpassword.text.toString().trim().isNotEmpty() ->
            {
                if (registeremail.text.toString().trim().matches(Regex( "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+" ))){
                    if (newpassword.text.toString().length >= 5){
                        if (newpassword.text.toString() == confirmpassword.text.toString()){
                            //firebaseAuthenticate()
                            //createAccount(registeremail.text.toString(),newpassword.text.toString())
                            launchSignInFlow()
//                            Toast.makeText(context, "Welcome " + username.text.toString(), Toast.LENGTH_SHORT).show()
                        }else{
                            confirmpassword.setError("Password doesn't match")
                        }
                    }else{
                        newpassword.setError("Password must be more than 5 characters")
                    }
                }else{
                    registeremail.setError("Please set a valid email")
                }
            }
        }
    }
    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            SIGN_IN_RESULT_CODE
        )
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                activity?.let {
                    val intent = Intent(it, NavDrawer::class.java)
                    it.startActivity(intent)
                }

                // User successfully signed in.
                Log.i(TAG, "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
            } else {
                // Sign in failed. If response is null, the user canceled the
                // sign-in flow using the back button. Otherwise, check
                // the error code and handle the error.
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment Register.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            Register().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}