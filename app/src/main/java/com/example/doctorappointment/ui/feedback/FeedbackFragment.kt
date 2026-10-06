package com.example.doctorappointment.ui.feedback

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.doctorappointment.databinding.FragmentFeedbackBinding

class FeedbackFragment : Fragment() {

    private var _binding: FragmentFeedbackBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSend.setOnClickListener {
            sendFeedback()
        }
    }

    private fun sendFeedback() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val message = binding.etMessage.text.toString().trim()

        // ========== ПРОВЕРКА ИМЕНИ ==========
        if (name.isEmpty()) {
            binding.etName.error = "Введите ваше имя"
            binding.etName.requestFocus()
            return
        }
        if (name.length < 2) {
            binding.etName.error = "Имя должно содержать минимум 2 символа"
            binding.etName.requestFocus()
            return
        }

        // ========== ПРОВЕРКА EMAIL ==========
        if (email.isEmpty()) {
            binding.etEmail.error = "Введите ваш email"
            binding.etEmail.requestFocus()
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Введите корректный email (example@mail.com)"
            binding.etEmail.requestFocus()
            return
        }

        // ========== ПРОВЕРКА СООБЩЕНИЯ ==========
        if (message.isEmpty()) {
            binding.etMessage.error = "Введите сообщение"
            binding.etMessage.requestFocus()
            return
        }
        if (message.length < 10) {
            binding.etMessage.error = "Сообщение должно содержать минимум 10 символов"
            binding.etMessage.requestFocus()
            return
        }

        // ========== ОТПРАВКА ==========
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSend.isEnabled = false
        binding.tvResult.visibility = View.GONE

        // Имитация отправки
        Handler(Looper.getMainLooper()).postDelayed({
            binding.progressBar.visibility = View.GONE
            binding.btnSend.isEnabled = true

            binding.tvResult.visibility = View.VISIBLE
            binding.tvResult.text = "✓ Спасибо, $name! Ваше сообщение отправлено."

            // Очищаем поля
            binding.etName.text.clear()
            binding.etEmail.text.clear()
            binding.etMessage.text.clear()

            // Скрываем сообщение через 3 секунды
            Handler(Looper.getMainLooper()).postDelayed({
                binding.tvResult.visibility = View.GONE
            }, 3000)

            Toast.makeText(requireContext(), "Сообщение отправлено!", Toast.LENGTH_SHORT).show()
        }, 1500)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}