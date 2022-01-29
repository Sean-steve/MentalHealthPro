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
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Login.newInstance] factory method to
 * create an instance of this fragment.
 */
class Login : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var signinbutton:Button
    private lateinit var signinemail:EditText
    private lateinit var signinpassword:EditText
    private lateinit var signuptext:TextView

    //firebase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            auth = Firebase.auth

        }
    }
//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        if(currentUser != null){
//            reload();
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View=inflater.inflate(R.layout.login, container, false)
        signinbutton=view.findViewById(R.id.loginbtn)
        signinemail=view.findViewById(R.id.signinemail)
        signinpassword=view.findViewById(R.id.signinpassword)
        signuptext=view.findViewById(R.id.signuptext)

        signinbutton.setOnClickListener{
            signinValidate()
        }
        signuptext.setOnClickListener {
            val fragment = Register()
            val fragmentManager = activity?.supportFragmentManager
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.nav_host_fragment, fragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
        return view
    }
    private fun firebaseAuthentication() {
        //if error;
        signinbutton.isEnabled = false
        signinbutton.alpha = 0.5f
        auth.signInWithEmailAndPassword(signinemail.text.toString(),
            signinpassword.text.toString()).addOnCompleteListener {
                task ->
            if (task.isSuccessful){
                activity?.let {
                    val intent = Intent(it, NavDrawer::class.java)
                    it.startActivity(intent)
                }

            }else{
                //if error

                signinbutton.isEnabled = true
                signinbutton.alpha = 1.0f
                Toast.makeText(context,task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun signinValidate() {
        when {

            TextUtils.isEmpty(signinemail.text.toString().trim()) -> {
                signinemail.setError("Please enter your email")
            }
            TextUtils.isEmpty(signinpassword.text.toString().trim()) -> {
                signinpassword.setError("Please enter your email")
            }


            signinemail.text.toString().trim().isNotEmpty() &&
                    signinpassword.text.toString().trim().isNotEmpty() -> {
                if (signinemail.text.toString().trim()
                        .matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
                ) {
                    //firebaseAuthentication()
                    launchSignInFlow()


                } else {
                    signinemail.setError("Please set a valid email")
                }
            }

        }
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
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
    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    activity?.let {
                        val intent = Intent(it, NavDrawer::class.java)
                        it.startActivity(intent)}
                    sendEmailVerification()
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }
    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener() { task ->
                // Email Verification sent
            }
    }
    private fun updateUI(user: FirebaseUser?) {

    }

    private fun reload() {

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
            Register.SIGN_IN_RESULT_CODE
        )
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Register.SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                activity?.let {
                    val intent = Intent(it, NavDrawer::class.java)
                    it.startActivity(intent)
                }
            }
        }
    }
                companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Login.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Login().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}