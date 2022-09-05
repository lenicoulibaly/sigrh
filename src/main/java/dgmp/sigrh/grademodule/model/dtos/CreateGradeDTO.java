package dgmp.sigrh.grademodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@UniqueGrade
public class CreateGradeDTO
{
    @Min(value = 1, message = "Le rang doit être compris entre 1 et 7")
    @Max(value = 7, message = "Le rang doit être compris entre 1 et 7")
    private int rang; //D = 1 , C = 2 ,  B = 3 , A = 4
    @ExistingCategoryName
    private String categorie;
}
