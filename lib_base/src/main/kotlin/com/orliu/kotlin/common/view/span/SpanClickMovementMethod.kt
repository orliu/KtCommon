package com.orliu.kotlin.common.view.span

import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.method.Touch
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.widget.TextView

/**
 * Created by orliu on 03/04/2018.
 */
class SpanClickMovementMethod : LinkMovementMethod() {

    companion object {
        fun instance(): SpanClickMovementMethod {
            return Holder.instance
        }
    }

    private object Holder {
        val instance = SpanClickMovementMethod()
    }

    override fun onTouchEvent(widget: TextView, buffer: Spannable,
                              event: MotionEvent): Boolean {
        val action = event.action

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            var x = event.x.toInt()
            var y = event.y.toInt()

            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop

            x += widget.scrollX
            y += widget.scrollY

            val layout = widget.layout
            val line = layout.getLineForVertical(y)
            val off = layout.getOffsetForHorizontal(line, x.toFloat())

            val link = buffer.getSpans(off, off, ClickableSpan::class.java)

            if (link.size != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(widget)

                } else if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0]))
                }

                if (widget is SpanClickTextView) {
                    widget.isContentClicked = true
                }

                return true
            } else {
                Selection.removeSelection(buffer)
                super.onTouchEvent(widget, buffer, event)
                return false
            }
        }

        return Touch.onTouchEvent(widget, buffer, event)
    }
}
// e.g:
///**
// * 客服繁忙消息文本设置
// *
// * @param textView
// * @param message
// */
//private void setHrefSpanText(SpanClickTextView textView, String message) {
//    String emailStr = ZendeskConfig.CUSTOMER_SERVICE_MAIL;
//
//    int emailStartPosition = message.indexOf(emailStr);
//    int emailEndPosition = emailStartPosition + emailStr.length();
//
//    String centreLinkStr = getString(R.string.chat_tapping_this_link);
//    int centreLinkStartPosition = message.indexOf(centreLinkStr);
//    int centreLinkEndPosition = centreLinkStartPosition + centreLinkStr.length();
//
//    SpannableString spannableString = new SpannableString(message);
//    spannableString.setSpan(new EmailLinkSpan(), emailStartPosition, emailEndPosition, Spanned.SPAN_MARK_MARK);
//    spannableString.setSpan(new CentreLinkSpan(), centreLinkStartPosition, centreLinkEndPosition, Spanned.SPAN_MARK_MARK);
//
//    textView.setText(spannableString);
//    textView.setMovementMethod(SpanClickMovementMethod.getInstance());
//     textView.setOnClickListener(....)
//}
//
///**
// * Email超链接
// */
//private class EmailLinkSpan extends ClickableSpan {
//
//    @Override
//    public void updateDrawState(TextPaint ds) {
//        super.updateDrawState(ds);
//
//        ds.setColor(Color.parseColor("#FFFFFF"));
//        ds.setUnderlineText(true);
//    }
//
//    @Override
//    public void onClick(View widget) {
//        openEmailActivity();
//    }
//}
//
///**
// * CentreLink超链接
// */
//private class CentreLinkSpan extends ClickableSpan {
//
//    @Override
//    public void updateDrawState(TextPaint ds) {
//        super.updateDrawState(ds);
//
//        ds.setColor(Color.parseColor("#FFFFFF"));
//        ds.setUnderlineText(true);
//    }
//
//    @Override
//    public void onClick(View widget) {
//
//        try {
//            Intent browse = new Intent(Intent.ACTION_VIEW);
//            browse.setData(Uri.parse(getString(R.string.chat_help_centre_link)));
//            browse.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(browse);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}