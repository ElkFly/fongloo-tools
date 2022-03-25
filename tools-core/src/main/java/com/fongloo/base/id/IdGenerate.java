package com.fongloo.base.id;

import java.io.Serializable;

public interface IdGenerate<T extends Serializable> {
    /**
     * id 生成器
     *
     * @retrun
     */
    T generate();
}
