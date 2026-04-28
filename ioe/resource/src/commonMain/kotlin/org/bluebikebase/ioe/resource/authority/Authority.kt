package org.bluebikebase.ioe.resource.authority

import org.bluebikebase.ioe.resource.transaction.Dispatcher

interface Authority<T, R> : Reception<T, R>, Dispatcher<R>
