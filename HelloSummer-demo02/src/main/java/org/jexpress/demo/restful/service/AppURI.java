package org.jexpress.demo.restful.service;

public interface AppURI {
    String CONTEXT_ROOT = "/service";
    String CURRENT_VERSION = "/v1";
    
    String API_NF_FILE_UPLOAD = CONTEXT_ROOT + CURRENT_VERSION + "/upload";
}
