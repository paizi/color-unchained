package draylar.cu.mixin;

import draylar.cu.client.gui.ColorButtonWidget;
import draylar.cu.client.gui.ColorToggleWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

/*
  Adds the color tab to the sign GUI.
 */
@Mixin(SignEditScreen.class)
public abstract class SignEditScreenMixin extends Screen {

    @Shadow private SelectionManager selectionManager;
    boolean showColors = false;

    ColorToggleWidget colorButton = new ColorToggleWidget(2, 2, 16, 16, new LiteralText("Color"), (widget) -> {
        toggleVisible();
    });

    private ArrayList<ColorButtonWidget> colors = new ArrayList<>();

    protected SignEditScreenMixin(Text text_1) {
        super(text_1);
    }

    private void toggleVisible() {
        showColors = !showColors;

        colors.forEach(color -> color.setVisiblity(showColors));
    }


    @Inject(at = @At("HEAD"), method = "init")
    private void init(CallbackInfo ci) {
        int index = 0;

        for(Formatting color : Formatting.values()) {
            index++;
            ColorButtonWidget red = new ColorButtonWidget(color,18 * index + 3, 2, 16, 16, new LiteralText(color.getName()), (widget) -> {
                selectionManager.insert('§');
                selectionManager.insert(((FormattingAccessor) (Object) color).getCode());
            });

            colors.add(red);
            addButton(red);
        }

        addButton(colorButton);
    }
}
