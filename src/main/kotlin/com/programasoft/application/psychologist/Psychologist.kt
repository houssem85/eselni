package com.programasoft.application.psychologist

import com.programasoft.application.account.Account
import com.programasoft.application.bankaccount.BankAccount
import com.programasoft.application.certificate.Certificate
import jakarta.persistence.*

@Entity
@Table(name = "psychologist")
data class Psychologist(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,
    @OneToOne(cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    val account: Account,
    val city: String,
    @Column(name = "full_address")
    val fullAddress: String,
    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    val image: ByteArray?,
    @Column(name = "phone_number")
    val phoneNumber: String,
    val presentation: String,
    @Column(name = "hourly_rate")
    val hourlyRate: Float,
    @Column(name = "message_rate")
    val messageRate: Float,
    @OneToMany(mappedBy = "psychologist", fetch = FetchType.EAGER)
    val certificates: Set<Certificate> = mutableSetOf(),
    @ElementCollection(targetClass = LegalArea::class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
        name = "psychologist_legal_areas",
        joinColumns = [JoinColumn(name = "psychologist_id")]
    )
    @Column(name = "legal_area")
    var legalAreas: Set<LegalArea> = emptySet(),
    @OneToOne(cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id")
    val bankAccount: BankAccount,
)

/**
{
"account": {
"email": "john.doe@example.com",
"password": "mysecretpassword"
},
"fullName": "John Doe",
"city": "Paris",
"fullAddress": "123 rue de la Paix, 75008 Paris",
"image": "ZWVn",
"phoneNumber": "+33 6 12 34 56 78",
"presentation": "Je suis avocat depuis plus de 10 ans...",
"hourlyRate": 200.0,
"messageRate": 1.5,
"certificates": [
{
"title": "Certificat en droit du travail",
"description": "J'ai obtenu ce certificat en 2015...",
"image": "ZWVn"
},
{

"title": "Certificat en droit de la famille",
"description": "J'ai obtenu ce certificat en 2018...",
"image": "ZWVn"
}
],
"legalAreas": [
"LABOR_LAW",
"FAMILY_LAW"
],
"bankAccount": {
"IBAN": "FR76 3000 2005 0000 0071 7202 K57",
"bank": "zitpuna",
"fullName":"houssem"
}
}
 * */