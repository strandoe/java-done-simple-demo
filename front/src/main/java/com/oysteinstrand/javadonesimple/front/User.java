package com.oysteinstrand.javadonesimple.front;

import com.oysteinstrand.javadonesimple.common.Key;

public interface User {
    Key<Long> id = new Key<Long>("id");
    Key<String> name = new Key<String>("name");
}
