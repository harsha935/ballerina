/*
*  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/
package org.ballerinalang.model.values;

import org.ballerinalang.model.types.BType;
import org.ballerinalang.util.exceptions.BLangExceptionHelper;
import org.ballerinalang.util.exceptions.RuntimeErrors;

import java.lang.reflect.Array;

/**
 * {@code BArray} represents an arrays in Ballerina.
 *
 * @since 0.87
 */
// TODO Change this class name
public abstract class BNewArray implements BRefType {

    /**
     * The maximum size of arrays to allocate.
     * <p>
     * This is same as Java
     */
    protected static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    protected static final int DEFAULT_ARRAY_SIZE = 100;

    protected int size = 0;

    public abstract void grow(int newLength);

    @Override
    public String stringValue() {
        return null;
    }

    @Override
    public BType getType() {
        return null; //todo
    }

    @Override
    public BRefType value() {
        return null;
    }


    // Private methods

    protected Object newArrayInstance(Class<?> componentType) {
        return Array.newInstance(componentType, DEFAULT_ARRAY_SIZE);
    }

    protected void prepareForAdd(long index, int currentArraySize) {
        int intIndex = (int) index;
        rangeCheck(index, size);
        ensureCapacity(intIndex, currentArraySize);
        resetSize(intIndex);
    }

    protected void resetSize(int index) {
        if (index >= size) {
            size = index + 1;
        }
    }

    protected void rangeCheck(long index, int size) {
        if (index > Integer.MAX_VALUE || index < Integer.MIN_VALUE) {
            throw BLangExceptionHelper.getRuntimeException(
                    RuntimeErrors.INDEX_NUMBER_TOO_LARGE, index);
        }

        if ((int) index < 0) {
            throw BLangExceptionHelper.getRuntimeException(
                    RuntimeErrors.ARRAY_INDEX_OUT_OF_RANGE, index, size);
        }
    }

    protected void rangeCheckForGet(long index, int size) {
        rangeCheck(index, size);
        if (index < 0 || index >= size) {
            throw BLangExceptionHelper.getRuntimeException(
                    RuntimeErrors.ARRAY_INDEX_OUT_OF_RANGE, index, size);
        }
    }

    protected void ensureCapacity(int capacity, int currentArraySize) {
        int y = capacity / DEFAULT_ARRAY_SIZE;
        int x = currentArraySize / DEFAULT_ARRAY_SIZE;

        if (y - x >= 0) {
            int newLength = (y + 1) * DEFAULT_ARRAY_SIZE;
            newLength = Math.min(newLength, MAX_ARRAY_SIZE);
            grow(newLength);
        }
    }

    public long size() {
        return size;
    }
}
