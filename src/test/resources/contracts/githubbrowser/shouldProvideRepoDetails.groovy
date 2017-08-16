package contracts

org.springframework.cloud.contract.spec.Contract.make {
    request { // (1)
        method 'PUT' // (2)
        url '/fraudcheck' // (3)
        body([ // (4)
               "client.id": $(regex('[0-9]{10}')),
               loanAmount: 99999
        ])
        headers { // (5)
            contentType('application/json')
        }
    }
    response { // (6)
        status 200 // (7)
        body([ // (8)
               fraudCheckStatus: "FRAUD",
               "rejection.reason": "Amount too high"
        ])
        headers { // (9)
            contentType('application/json')
        }
    }
}