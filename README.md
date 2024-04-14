## ChannelFlow
ChannelFlow and flow have difference purpose. Channels facilitate communication between coroutines, while Flows are more about efficient data production and distribution.
ChannelFlow is based on Channels. 

1. ChannelFlow is more slower than flow when they emit one more than flows.
```
val flows = channelFlow {
    launch {
        send(KoreaHelper.changeDates())
    }

    launch {
        send(USHelper.changeDates())
    }

    launch {
        send(JapanHelper.changeDates())
    }
}

// At the place you need the data
flows.collect { data ->
    // Do something
}
```

2. ChannelFlow has default 64 buffers.

But, ChannelFlows could produce data asynchronously and mange each scope independently. if it has several coroutines, you can cancel some of them. it dosen't matter for it. it is differenct from flow.

#### Reference
- https://proandroiddev.com/why-use-flow-if-we-have-the-powerful-channelflow-in-mobile-development-1e8e718c80ea
- https://medium.com/mobile-app-development-publication/kotlins-flow-channelflow-and-callbackflow-made-easy-5e82ce2e27c0
