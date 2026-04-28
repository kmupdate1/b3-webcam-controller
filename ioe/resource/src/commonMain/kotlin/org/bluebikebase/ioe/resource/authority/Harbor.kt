package org.bluebikebase.ioe.resource.authority

import org.bluebikebase.ioe.resource.transaction.Dispatcher

interface Harbor<T, R> : Reception<T, R>, Dispatcher<R>
