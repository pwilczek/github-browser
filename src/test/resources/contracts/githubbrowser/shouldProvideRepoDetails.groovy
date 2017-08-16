package contracts

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url $(
                consumer(regex('/repositories/[a-zA-Z0-9]+/[a-zA-Z0-9]+')),
                producer('/repositories/pw/githubbrowser')
        )
    }
    response {
        status 200
        body([
                'fullName'   : 'GitHub Browser',
                'description': 'With this app you can browse repos',
                'cloneUrl'   : 'https://github.com/pw/githubbrowser.git',
                'stars'      : 42,
                'createdAt'  : '2017-08-16'
        ])
        headers {
            contentType('application/json')
        }
    }
}