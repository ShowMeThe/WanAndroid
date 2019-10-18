package showmethe.github.core.glide

import com.bumptech.glide.load.Key
import com.bumptech.glide.util.Preconditions

import java.security.MessageDigest



/**
 * showmethe.github.core.glide
 *
 * 2019/1/10
 **/

class ObjectKey(var key: Any) : Key {

    val ob : Any = Preconditions.checkNotNull(key)


    override fun toString(): String {
        return ("ObjectKey{"
                + "object=" + ob
                + '}'.toString())
    }

    override fun equals(o: Any?): Boolean {
        if (o is ObjectKey) {
            val other = o as ObjectKey?
            return ob == other!!.ob
        }
        return false
    }

    override fun hashCode(): Int {
        return ob.hashCode()
    }

    // Charset CHARSET = Charset.forName("UTF-8");
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ob.toString().toByteArray(Key.CHARSET))
    }
}
