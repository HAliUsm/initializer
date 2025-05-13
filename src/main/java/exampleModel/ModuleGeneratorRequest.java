package exampleModel;

import io.americanexpress.synapse.service.imperative.model.BaseServiceRequest;
import lombok.Data;

import java.util.List;

@Data
public class ModuleGeneratorRequest implements BaseServiceRequest {

    private List<ModuleData> apiModules;

    private List<ModuleData> clientModules;

    private List<ModuleData> serviceModules;

    private List<ModuleData> dataModules;

    private List<ModuleData> subscriberModules;
}
