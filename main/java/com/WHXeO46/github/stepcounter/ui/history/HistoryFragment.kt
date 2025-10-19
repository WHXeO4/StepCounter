package com.WHXeO46.github.stepcounter.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.WHXeO46.github.stepcounter.databinding.FragmentHistoryBinding
import com.WHXeO46.github.stepcounter.user.Account
import android.graphics.Color
import android.widget.LinearLayout
import android.widget.TextView

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val historyContainer = binding.historyContainer
        val context = requireContext()
        val history = Account.getHistory()

        if (history.isEmpty()) {
            val textView = TextView(context).apply {
                text = "还没有数据喵，请多用一段时间喵"
                textSize = 16f
                setPadding(8, 8, 8, 8)
            }
            historyContainer.addView(textView)
        } else {

            history.reversed().forEach { blob ->

                val textView = TextView(context).apply {
                    text = "日期：${blob.date}，步骤：${blob.step}"
                    textSize = 16f
                    setPadding(8, 8, 8, 8)
                }
                historyContainer.addView(textView)

                // 创建分隔符
                val divider = View(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2
                    ).apply {
                        setMargins(0, 8, 0, 8)
                    }
                    setBackgroundColor(Color.BLACK)
                }
                historyContainer.addView(divider)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}