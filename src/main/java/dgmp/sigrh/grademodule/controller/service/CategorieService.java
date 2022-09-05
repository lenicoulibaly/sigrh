package dgmp.sigrh.grademodule.controller.service;

import dgmp.sigrh.grademodule.model.enums.Categorie;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieService implements ICategorieService
{
    @Override
    public List<Categorie> getAllCategories()
    {
        return EnumUtils.getEnumList(Categorie.class);
    }

    @Override
    public long getRang(String catName)
    {
        return EnumUtils.getEnum(Categorie.class, catName).rang;
    }
}
