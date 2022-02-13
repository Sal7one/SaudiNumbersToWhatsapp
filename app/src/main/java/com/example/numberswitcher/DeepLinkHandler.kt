package com.example.numberswitcher

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DeepLinkHandler : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainIntent = intent
        val fromShareMenu = mainIntent.extras?.getString(Intent.EXTRA_TEXT)

        if (fromShareMenu != null) {
            var numberWithTelSchema = fromShareMenu.toString()
            numberWithTelSchema = numberWithTelSchema.replace(" ", "")
            numberWithTelSchema = numberWithTelSchema.replace("tel:", "")
            numberHandler(numberWithTelSchema)
        } else {
            val fromContextMenu = mainIntent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
            numberHandler(fromContextMenu.toString())
        }
    }

    private fun numberHandler(phoneNumber: String) {
        var phone = phoneNumber
        phone = phone.replace(" ", "")

        if (phone.startsWith("05"))
            phone = "966" + phone.substring(phone.indexOf("05") + 1)
        else if (phone.startsWith("+"))
            phone = phone.substring(phone.indexOf("+") + 1)

        val saudiNumberRegex = "((\\+)?(966([_ \\-])?|0)5[0-9]{8})"
        val isASaudiPhoneNumber = phone.contains(Regex(saudiNumberRegex))

        if (isASaudiPhoneNumber) {
            switchToApp(phone)
        } else {
            Toast.makeText(this, "Not a Saudi Number", Toast.LENGTH_SHORT).show()
            onStop()
        }
    }

    private fun switchToApp(cleanPhone: String) {
        val whatsAppURI = "https://api.whatsapp.com/send?phone="
        val uri = Uri.parse(whatsAppURI + cleanPhone)
        val i = Intent(Intent.ACTION_VIEW, uri)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
    }

    override fun onStop() {
        super.onStop()
        finishAndRemoveTask()
    }
}