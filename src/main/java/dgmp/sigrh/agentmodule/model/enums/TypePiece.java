package dgmp.sigrh.agentmodule.model.enums;

import java.util.Arrays;

public enum TypePiece
{
    CNI("CNI"), ATTESTATION("Attestation"), PERMI_DE_CONDUIRE("Permi de conduire"), PASSEPORT("Passeport");
    private final String name;
    TypePiece(String name) {this.name = name;}

    @Override
    public String toString() { return name; }
    public static boolean existsByValue(String value) { return Arrays.stream(TypePiece.values()).anyMatch(val->value.equalsIgnoreCase(val.toString()));}
}
