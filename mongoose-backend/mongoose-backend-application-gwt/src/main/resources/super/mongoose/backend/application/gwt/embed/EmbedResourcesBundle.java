// Generated by Webfx
package mongoose.backend.application.gwt.embed;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import webfx.platform.gwt.services.resource.spi.impl.GwtResourceBundleBase;

public interface EmbedResourcesBundle extends ClientBundle {

    EmbedResourcesBundle R = GWT.create(EmbedResourcesBundle.class);

    @Source("images/svg/color/price-tag.svg")
    TextResource r1();

    @Source("images/svg/mono/calendar.svg")
    TextResource r2();

    @Source("images/svg/mono/certificate.svg")
    TextResource r3();

    @Source("images/svg/mono/price-tag.svg")
    TextResource r4();

    @Source("mongoose/client/services/i18n/dictionaries/en.json")
    TextResource r5();

    @Source("mongoose/client/services/i18n/dictionaries/fr.json")
    TextResource r6();

    @Source("mongoose/shared/domainmodel/DomainModelSnapshot.json")
    TextResource r7();

    @Source("webfx/platform/client/services/websocketbus/conf/BusOptions.json")
    TextResource r8();

    final class ProvidedGwtResourceBundle extends GwtResourceBundleBase {
        public ProvidedGwtResourceBundle() {
            registerResource("images/svg/color/price-tag.svg", R.r1());
            registerResource("images/svg/mono/calendar.svg", R.r2());
            registerResource("images/svg/mono/certificate.svg", R.r3());
            registerResource("images/svg/mono/price-tag.svg", R.r4());
            registerResource("mongoose/client/services/i18n/dictionaries/en.json", R.r5());
            registerResource("mongoose/client/services/i18n/dictionaries/fr.json", R.r6());
            registerResource("mongoose/shared/domainmodel/DomainModelSnapshot.json", R.r7());
            registerResource("webfx/platform/client/services/websocketbus/conf/BusOptions.json", R.r8());
        }
    }
}
