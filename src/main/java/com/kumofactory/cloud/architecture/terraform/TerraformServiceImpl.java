package com.kumofactory.cloud.architecture.terraform;

import org.springframework.stereotype.Service;

@Service
public class TerraformServiceImpl implements TerraformService {

  /**
   * 1. Client 에서 요청이 들어오면, Controller 에서 요청을 받는다. 2. Client 에서 요청한 Parameter 를 사용하여 main.tf 파일을
   * 작성한다. main.tf 작성 순서 -> VPC, Security Group, Subnet, EC2, 3. main.tf 파일을 사용하여 Terraform apply 를
   * 실행한다. 4. output.tf 파일을 사용해서 output 값을 가져온다. 5. output 값과 parameter 값을 database 에 저장한다. 6. 작업이
   * 완료되면 Client 에게 결과를 전달한다.
   */
  @Override
  public void deploy() {
  }


}
