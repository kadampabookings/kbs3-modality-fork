package org.modality_project.base.client.activity;

import javafx.scene.control.Button;
import org.modality_project.base.client.activity.themes.Theme;
import dev.webfx.stack.framework.client.ui.controls.button.ButtonFactoryMixin;
import dev.webfx.stack.framework.client.ui.controls.button.ButtonBuilder;
import dev.webfx.stack.framework.client.ui.controls.MaterialFactoryMixin;

/**
 * @author Bruno Salmon
 */
public interface ModalityButtonFactoryMixin extends ButtonFactoryMixin, MaterialFactoryMixin {

    @Override
    default Button styleButton(Button button) {
        button.textFillProperty().bind(Theme.mainTextFillProperty());
        return button;
    }

    default Button newBookButton() {
        return newBookButtonBuilder().build();
    }

    default ButtonBuilder newBookButtonBuilder() {
        return newColorButtonBuilder("Book>>", "#7fd504", "#2a8236");
    }

    default Button newSoldoutButton() {
        return newSoldoutButtonBuilder().build();
    }

    default ButtonBuilder newSoldoutButtonBuilder() {
        return newColorButtonBuilder("Soldout", "#e92c04", "#853416");
    }

}
