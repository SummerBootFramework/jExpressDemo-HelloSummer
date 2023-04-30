package org.jexpress.demo.restful.vo;

import org.summerboot.jexpress.boot.BootPOI;
import org.summerboot.jexpress.boot.annotation.Unique;

/**
 * Please try to keep each of the string content short
 *
 * @author Changski Tie Zheng Zhang
 */
@Unique(name = "POI", type = String.class)
public interface AppPOI extends BootPOI {

    String FILE_BEGIN = "file.begin";
    String FILE_END = "file.end";
}
