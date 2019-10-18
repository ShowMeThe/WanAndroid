package showmethe.github.core.dialog

import android.os.Bundle
import android.view.Gravity

import showmethe.github.core.R
import showmethe.github.core.widget.animView.BallRotationProgressBar

@WindowParam(gravity = Gravity.CENTER,noAnim = true)
class DialogLoading : SimpleDialogFragment() {

    internal var progressbar: BallRotationProgressBar? = null
    override fun build(savedInstanceState: Bundle?) {
        buildDialog {
            R.layout.dialog_loading_layout
        }
    }
}
