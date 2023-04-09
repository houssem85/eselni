package com.programasoft.application.advocate

enum class LegalArea(val id: Int, val displayName: String, val description: String) {
    LABOR_LAW(1, "Droit du travail", "Spécialisé dans les relations entre employeurs et salariés"),
    FAMILY_LAW(2, "Droit de la famille", "Spécialisé dans les affaires familiales, telles que le divorce, la garde d'enfants, etc."),
    CRIMINAL_LAW(3, "Droit pénal", "Spécialisé dans les affaires pénales, telles que les crimes, les délits, etc."),
    CONTRACT_LAW(4, "Droit des contrats", "Spécialisé dans les contrats entre les particuliers et les entreprises"),
    PROPERTY_LAW(5, "Droit immobilier", "Spécialisé dans les questions relatives à la propriété immobilière"),
    INTELLECTUAL_PROPERTY_LAW(6, "Droit de la propriété intellectuelle", "Spécialisé dans les brevets, les marques, les droits d'auteur, etc."),
    ENVIRONMENTAL_LAW(7, "Droit de l'environnement", "Spécialisé dans les questions liées à l'environnement et à la protection de la nature"),
    TAX_LAW(8, "Droit fiscal", "Spécialisé dans les questions fiscales et les impôts"),
    INTERNATIONAL_LAW(9, "Droit international", "Spécialisé dans les questions juridiques internationales et transfrontalières");

    companion object {
        fun fromId(id: Int): LegalArea? = values().find { it.id == id }
    }
}





