package __modulePackage__.controller;

import __modulePackage__.model.__requestClassNameUCamel__;
import __modulePackage__.model.__responseClassNameUCamel__;
import __modulePackage__.model.__serviceClassNameUCamel__;
import io.americanexpress.synapse.service.imperative.controller.BaseDeleteImperativeRestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@code __controllerClassNameUCamel__}.
 *
 * @author __authors__
 */
@RequestMapping(__endpoint__)
@RestController
public class __controllerClassNameUCamel__ extends BaseDeleteImperativeRestController<
        __requestClassNameUCamel__,
        __responseClassNameUCamel__,
        __serviceClassNameUCamel__> {
}