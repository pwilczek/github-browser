package contracts

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url $(
                consumer(regex('/repositories/[a-zA-Z0-9-]+/[a-zA-Z0-9-]+')),
                producer('/repositories/spring-cloud/spring-cloud-contract')
        )
    }
    response {
        status 200
        body([
                'fullName'   : 'spring-cloud/spring-cloud-contract',
                'description': 'Support for Consumer Driven Contracts in Spring',
                'cloneUrl'   : 'https://github.com/spring-cloud/spring-cloud-contract.git',
                'stars'      : 150,
                'createdAt'  : '2016-06-12'
        ])
        headers {
            contentType('application/json')
        }
    }
}