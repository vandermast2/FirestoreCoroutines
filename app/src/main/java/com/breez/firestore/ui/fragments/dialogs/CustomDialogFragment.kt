package com.breez.firestore.ui.fragments.dialogs

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.breez.firestore.R
import com.breez.firestore.model.AnnounceModel
import com.breez.firestore.ui.activities.MainViewModel
import com.breez.firestore.util.Constants
import com.breez.firestore.util.MiscellaneousUtils
import com.breez.firestore.util.onClick
import com.breez.firestore.util.toast
import com.jakewharton.rxbinding.widget.RxTextView
import kotlinx.android.synthetic.main.delete_dialog.*
import kotlinx.android.synthetic.main.edit_dialog.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


@InternalCoroutinesApi
class CustomDialogFragment : DialogFragment() {
    private lateinit var vm: MainViewModel
    private var announceModel: AnnounceModel? = null

    companion object {
        private val EXTRA_COLLECTION_ID = MiscellaneousUtils.getExtra("collectionId")
        private val EXTRA_TYPE_ID = MiscellaneousUtils.getExtra("typeId")
        fun newInstance(collectionId: String?, typeId: String?) = CustomDialogFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_COLLECTION_ID, collectionId ?: "")
                putString(EXTRA_TYPE_ID, typeId ?: "")
            }
        }
    }

    private val typeId by lazy {
        arguments?.getString(EXTRA_TYPE_ID)
            ?: ""
    }

    private val collectionId by lazy {
        arguments?.getString(EXTRA_COLLECTION_ID)
            ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return when (typeId) {
            Constants.EDIT_TYPE -> {
                inflater.inflate(R.layout.edit_dialog, null, true)
            }
            else -> {
                inflater.inflate(R.layout.delete_dialog, null, true)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        setFields()
        changeType()
        validation(etTel)
    }

    private fun changeType() {
        when (typeId) {
            Constants.EDIT_TYPE -> {
                btnSave.onClick {
                    if (checkIsEmpty()) {
                        if (collectionId.isNotEmpty()) {
                            update(collectionId, createMap())
                        } else {
                            save(createMap())
                        }
                    }
                }
            }
            else -> {
                btnYes.onClick { deleteItem(collectionId) }
                btnNo.onClick { close() }
            }
        }
    }

    private fun checkIsEmpty(): Boolean {
        return if (etTitle.text.isEmpty() or etCity.text.isEmpty() or etMessage.text.isEmpty() or etPrice.text.isEmpty() or etName.text.isEmpty() or etTel.text.isEmpty()) {
            context!!.toast("Все поля должны быть заполнены!")
            false
        } else true
    }

    private fun setFields() {
        GlobalScope.launch {
            announceModel = vm.getByID(collectionId)
            setView(announceModel)
        }
    }

    private fun deleteItem(id: String) {
        GlobalScope.async { vm.delete(id) }
        close()
    }

    private fun createMap(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map["title"] = etTitle.text.toString()
        map["message"] = etMessage.text.toString()
        map["city"] = etCity.text.toString()
        map["name"] = etName.text.toString()
        map["price"] = etPrice.text.toString()
        map["tel"] = etTel.text.toString()
        return map
    }

    private fun validation(v: EditText?) {
        if (v != null) {
            RxTextView.textChanges(v)
                .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .map { charSequence -> charSequence.toString() }
                .subscribe { it ->
                    if (isValidPhone(it) or it.isEmpty()) {
                        txtTel.setTextColor(Color.BLACK)
                    } else {
                        txtTel.setTextColor(Color.RED)
                    }
                }
        }
    }

    private fun isValidPhone(phone: String): Boolean = if (!Pattern.matches(
            "\t\n" +
                    "(^\\+[0-9]{2}|^\\+[0-9]{2}\\(0\\)|^\\(\\+[0-9]{2}\\)\\(0\\)|^00[0-9]{2}|^0)([0-9]{9}\$|[0-9\\-\\s]{10}\$)",
            phone
        )
    ) {
        !(phone.length < 6 || phone.length > 13)
    } else {
        false
    }

    private fun save(map: HashMap<String, Any>) {
        GlobalScope.async { vm.addItem(map) }
        close()
    }

    private fun update(id: String, map: HashMap<String, Any>) {
        GlobalScope.async { vm.update(id, map) }
        close()
    }

    private fun setView(announceModel: AnnounceModel?) {
        if (announceModel != null) {
            with(announceModel) {
                etTitle?.setText(title, TextView.BufferType.EDITABLE)
                etMessage?.setText(message, TextView.BufferType.EDITABLE)
                etName?.setText(name, TextView.BufferType.EDITABLE)
                etCity?.setText(city, TextView.BufferType.EDITABLE)
                etPrice?.setText(price, TextView.BufferType.EDITABLE)
                etTel?.setText(tel, TextView.BufferType.EDITABLE)
            }
        }
    }

    private fun close() {
        this.dismiss()
    }
}