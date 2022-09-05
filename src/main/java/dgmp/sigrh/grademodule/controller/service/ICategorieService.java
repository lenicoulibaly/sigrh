package dgmp.sigrh.grademodule.controller.service;

import dgmp.sigrh.grademodule.model.enums.Categorie;

import java.util.List;

public interface ICategorieService
{
    List<Categorie> getAllCategories();
    long getRang(String catName);
}
