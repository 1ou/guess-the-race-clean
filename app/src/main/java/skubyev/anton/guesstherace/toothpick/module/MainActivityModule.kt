package skubyev.anton.guesstherace.toothpick.module

import skubyev.anton.guesstherace.presentation.global.GlobalMenuController
import toothpick.config.Module

class MainActivityModule : Module() {
    init {
        bind(GlobalMenuController::class.java).toInstance(GlobalMenuController())
    }
}