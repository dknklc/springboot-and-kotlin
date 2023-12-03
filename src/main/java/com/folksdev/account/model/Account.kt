package com.folksdev.account.model

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
data class Account(
        @Id
        @GeneratedValue(generator = "UUID")
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        val id: String? = "",
        val balance: BigDecimal? = BigDecimal.ZERO,
        val creationDate: LocalDateTime = LocalDateTime.now(),

        @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        @JoinColumn(name = "customer_id", nullable = false)
        val customer: Customer? = Customer(),

        @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = [CascadeType.ALL]) // "account" ilişkiyi sağlayan objenin ismi. Yani transcation classında ki Account objesinin ismi.
        val transaction: Set<Transaction> = HashSet()

        // cascade = [CascadeType.ALL] means Account kaydederken Transactionı da kaydet hacı demek.
) {

    constructor(customer: Customer, balance: BigDecimal, creationDate: LocalDateTime) : this(
            id = "",
            customer = customer,
            balance = balance,
            creationDate = creationDate
    )



    // Account modelinde biz ManyToOne kullandığımız için, hashCode functionını override etmemiz lazım. Çünkü ben veritabanında veriyi çekerken hibernate burda ki hashCode değerinin karşılaştırmasını yapıyor.
    // Eğer ben buraya customer ve transaction eklersem hashCode'a, bu sefer customer tablosunda ki verileri de çekmeye çalışıyor, onu çekerken account bilgisini çekmeye çalışıyor çünkü bu iş hashCode üzerinden yapılıyor.
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Account

        if (id != other.id) return false
        if (balance != other.balance) return false
        if (creationDate != other.creationDate) return false
        if (customer != other.customer) return false
        return transaction == other.transaction
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (balance?.hashCode() ?: 0)
        result = 31 * result + creationDate.hashCode()
        result = 31 * result + (customer?.hashCode() ?: 0)
        //result = 31 * result + (transaction?.hashCode() ?: 0)
        return result
    }
}
