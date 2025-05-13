package exampleModel;

import io.americanexpress.synapse.service.imperative.model.BaseServiceResponse;
import lombok.Data;

@Data
public class ModuleGeneratorResponse implements BaseServiceResponse {

    byte[] zipBytes;
}
