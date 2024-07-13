package one.modality.ecommerce.payment.buscall.serial;

import dev.webfx.platform.ast.AstObject;
import dev.webfx.platform.ast.ReadOnlyAstObject;
import dev.webfx.stack.com.serial.spi.impl.SerialCodecBase;
import one.modality.ecommerce.payment.CancelPaymentArgument;

/**
 * @author Bruno Salmon
 */
public final class CancelPaymentArgumentSerialCodec extends SerialCodecBase<CancelPaymentArgument> {

    private static final String CODEC_ID = "CancelPaymentArgument";

    private static final String PAYMENT_PRIMARY_KEY_KEY = "payment";

    public CancelPaymentArgumentSerialCodec() {
        super(CancelPaymentArgument.class, CODEC_ID);
    }

    @Override
    public void encode(CancelPaymentArgument arg, AstObject serial) {
        encodeObject( serial, PAYMENT_PRIMARY_KEY_KEY, arg.getPaymentPrimaryKey());
    }

    @Override
    public CancelPaymentArgument decode(ReadOnlyAstObject serial) {
        return new CancelPaymentArgument(
                decodeObject( serial, PAYMENT_PRIMARY_KEY_KEY)
        );
    }
}
