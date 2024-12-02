package org.jexpress.demo.restful.service.vo;

import org.summerboot.jexpress.boot.BootPOI;
import org.summerboot.jexpress.boot.annotation.Unique;

@Unique(name = "POI", type = String.class)
public interface AppPOI extends BootPOI {

    String FILE_BEGIN = "file.begin";
    String FILE_END = "file.end";
}
