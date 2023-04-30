package org.jexpress.demo.restful.vo;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.summerboot.jexpress.boot.BootErrorCode;
import org.summerboot.jexpress.boot.annotation.Unique;

/**
 *
 * @author Changski Tie Zheng Zhang
 */
@Unique(name = "ErrorCode", type = int.class)
public interface AppErrorCode extends BootErrorCode {

    interface CustomHttpStatus {

        HttpResponseStatus UNAVAILABLE_FOR_LEGAL_REASONS = HttpResponseStatus.valueOf(451, "Unavailable For Legal Reasons");
        HttpResponseStatus ABP_POSSIBLE_REJECTION = HttpResponseStatus.valueOf(520, "ABP Possible rejection");
    }

    // Generic: 1000 - 1099
    int GSERVICE_ERROR = 100;

}
