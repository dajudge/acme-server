/*
 * Copyright 2020 Alex Stockinger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.dajudge.acme.account.facade;

import com.dajudge.acme.account.facade.transport.AuthorizationRequestTO;
import com.dajudge.acme.account.facade.transport.OrderTO;

import java.util.List;

public interface OrderFacade {
    OrderTO createOrder(String accountId, List<AuthorizationRequestTO> orderIdentifiers);

    OrderTO finalizeOrder(String orderId, byte[] csr);

    OrderTO getOrder(String accountId, String orderId);

    List<OrderTO> listOrders(String accountId);
}
