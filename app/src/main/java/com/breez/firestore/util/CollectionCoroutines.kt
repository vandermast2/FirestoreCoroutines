package com.breez.firestore.util

import com.breez.firestore.model.Model
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@InternalCoroutinesApi
suspend fun <T : Model> CollectionReference.await(clazz: Class<T>): List<T> {
    return await { documentSnapshot ->
        Timber.d("${documentSnapshot.id} => ${documentSnapshot.data}")
        documentSnapshot.toObject(clazz)?.withId(documentSnapshot.id) as T
    }
}

@InternalCoroutinesApi
suspend fun <T : Model> CollectionReference.await(parser: (documentSnapshot: DocumentSnapshot) -> T): List<T> {
    return suspendCancellableCoroutine { continuation ->
        get().addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                val list = arrayListOf<T>()
                it.result?.forEach { list.add(parser.invoke(it)) }

                continuation.resume(list)
            } else if (it.exception != null) {
                continuation.resumeWithException(it.exception!!)
            } else {
                continuation.resumeWithException(EmptyStackException())
            }
        }

        continuation.invokeOnCancellation {
            if (continuation.isCancelled)
                try {
                    NonCancellable.cancel()
                } catch (ex: Throwable) {
                    //Ignore cancel exception
                }
        }
    }
}

@InternalCoroutinesApi
suspend fun CollectionReference.addAwait(value: Map<String, Any>): DocumentReference {
    return suspendCancellableCoroutine { continuation ->
        add(value).addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                Timber.d("Value add to firestore: ${it.result?.id}")
                continuation.resume(it.result!!)
            } else if (it.exception != null) {
                continuation.resumeWithException(it.exception!!)
            } else {
                continuation.resumeWithException(EmptyStackException())
            }
        }

        continuation.invokeOnCancellation {
            if (continuation.isCancelled)
                try {
                    NonCancellable.cancel()
                } catch (ex: Throwable) {
                    //Ignore cancel exception
                }
        }
    }
}

@InternalCoroutinesApi
suspend fun DocumentReference.deleteAwait() {
    return suspendCancellableCoroutine { continuation ->
        delete().addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(it.exception!!)
            }
        }

        continuation.invokeOnCancellation {
            if (continuation.isCancelled)
                try {
                    NonCancellable.cancel()
                } catch (ex: Throwable) {
                    //Ignore cancel exception
                }
        }
    }
}

@InternalCoroutinesApi
suspend fun DocumentReference.updateAwait(var1: Map<String, Any>) {
    return suspendCancellableCoroutine { continuation ->
        update(var1).addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(it.exception!!)
            }
        }

        continuation.invokeOnCancellation {
            if (continuation.isCancelled)
                try {
                    NonCancellable.cancel()
                } catch (ex: Throwable) {
                    //Ignore cancel exception
                }
        }
    }
}

@InternalCoroutinesApi
suspend fun DocumentReference.updateAwait(var1: FieldPath, var2: Any, var3: List<Any>) {
    return suspendCancellableCoroutine { continuation ->
        update(var1, var2, var3).addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(it.exception!!)
            }
        }

        continuation.invokeOnCancellation {
            if (continuation.isCancelled)
                try {
                    NonCancellable.cancel()
                } catch (ex: Throwable) {
                    //Ignore cancel exception
                }
        }
    }
}

@InternalCoroutinesApi
suspend fun DocumentReference.updateAwait(var1: String, var2: Any, var3: List<Any>) {
    return suspendCancellableCoroutine { continuation ->
        update(var1, var2, var3).addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(it.exception!!)
            }
        }

        continuation.invokeOnCancellation {
            if (continuation.isCancelled)
                try {
                    NonCancellable.cancel()
                } catch (ex: Throwable) {
                    //Ignore cancel exception
                }
        }
    }
}

@InternalCoroutinesApi
suspend fun DocumentReference.setAwait(var1: Any) {
    return suspendCancellableCoroutine { continuation ->
        set(var1).addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(it.exception!!)
            }
        }

        continuation.invokeOnCancellation {
            if (continuation.isCancelled)
                try {
                    NonCancellable.cancel()
                } catch (ex: Throwable) {
                    //Ignore cancel exception
                }
        }
    }
}