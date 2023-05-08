/* 
 *  Copyright (c) 2023, SJE2D
 *  
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without modification,
 *  are permitted provided that the following conditions are met:
 *  
 *      * Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *      * Neither the name of BlockProject 3D nor the names of its contributors
 *        may be used to endorse or promote products derived from this software
 *        without specific prior written permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.yuri6037.sje2d.asset.config;

import com.github.yuri6037.sje2d.asset.engine.system.IAssetFactory;
import com.github.yuri6037.sje2d.asset.engine.system.IAssetProtocol;
import com.github.yuri6037.sje2d.asset.engine.system.ITypeRegistry;

import java.util.HashMap;

public final class TypeRegistry implements ITypeRegistry {
    private final HashMap<String, IAssetFactory> factoryMap;
    private final HashMap<String, IAssetProtocol> protocolMap;

    private TypeRegistry(final HashMap<String, IAssetFactory> factoryMap,
                         final HashMap<String, IAssetProtocol> protocolMap) {
        this.protocolMap = protocolMap;
        this.factoryMap = factoryMap;
    }

    @Override
    public IAssetFactory getFactory(final String mimeType) {
        return factoryMap.get(mimeType.toUpperCase());
    }

    @Override
    public IAssetProtocol getProtocol(final String protocol) {
        return protocolMap.get(protocol.toUpperCase());
    }

    public static class Builder {
        private final HashMap<String, IAssetFactory> factoryMap = new HashMap<>();
        private final HashMap<String, IAssetProtocol> protocolMap = new HashMap<>();

        /**
         * Adds a new factory to the registry.
         * @param factory the factory to add.
         * @return this.
         */
        public Builder addFactory(final IAssetFactory factory) {
            factoryMap.put(factory.getMimeType().toUpperCase(), factory);
            return this;
        }

        /**
         * Adds a new protocol to the registry.
         * @param protocol the protocol to add.
         * @return this.
         */
        public Builder addProtocol(final IAssetProtocol protocol) {
            protocolMap.put(protocol.getName().toUpperCase(), protocol);
            return this;
        }

        /**
         * Builds this registry and returns the corresponding TypeRegistry.
         * @return a new instance of a TypeRegistry.
         */
        public TypeRegistry build() {
            return new TypeRegistry(factoryMap, protocolMap);
        }
    }
}
