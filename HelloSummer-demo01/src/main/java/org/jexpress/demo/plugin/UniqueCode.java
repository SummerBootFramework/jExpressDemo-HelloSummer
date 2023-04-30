package org.jexpress.demo.plugin;

import org.summerboot.jexpress.boot.BootErrorCode;
import org.summerboot.jexpress.boot.annotation.Unique;

/**
 *
 * @author Changski Tie Zheng Zhang
 */
@Unique(name = "UniqueCode", type = int.class)
public interface UniqueCode extends BootErrorCode {

    int GSERVICE_ERROR1 = 100;

    int GSERVICE_ERROR2 = 101;
}
