import com.google.common.base.Strings;
import org.junit.Before;
import org.junit.Test;
import rise.vision.APIWrapper;
import rise.vision.apis.responses.blocks.*;
import rise.vision.apis.utils.BlocksQueryBuilder;
import rise.vision.apis.utils.BlocksQueryBuilder.OrderKind;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class BlocksApiTest {
  APIWrapper wrapper;

  @Before
  public void before() {
    wrapper = new APIWrapper();
  }

  @Test
  public void getGenesisBlock() throws Exception {
    BlockResponse blockResponse = wrapper.blocksAPI.getBlock("13191140260435645922").execute().body();
    assertTrue(blockResponse.success);
    assertEquals(
      "a9e12f49432364c1e171dd1f9e2d34f8baffdda0f4ef0989d9439e5d46aba55ef640149ff7ed7d3e3d4e1ad4173ecfd1cc0384f5598175c15434e0d97ce4150d",
      blockResponse.block.blockSignature
    );

    assertEquals(1, blockResponse.block.height);

    assertThat(
      "confirmations",
      blockResponse.block.confirmations,
      greaterThan(1000000));

    assertNull(blockResponse.block.previousBlock);

    assertEquals(
      "35526f8a1e2f482264e5d4982fc07e73f4ab9f4794b110ceefecd8f880d51892",
      blockResponse.block.generatorPublicKey
    );
    assertEquals(
      303,
      blockResponse.block.numberOfTransactions
    );
    assertEquals(
      0,
      blockResponse.block.timestamp
    );

    assertEquals(
      10999999991000000L,
      blockResponse.block.totalAmount
    );

    assertEquals(
      0,
      blockResponse.block.version
    );

    assertEquals(
      0,
      blockResponse.block.reward
    );
    assertEquals(
      0,
      blockResponse.block.totalForged
    );
  }

  @Test
  public void getBlocks() throws Exception {
    BlocksResponse blocks = wrapper.blocksAPI.getBlocks(BlocksQueryBuilder.builder()
      .orderBy(OrderKind.HEIGHT_ASC)
      .limit(30)
      .offset(2)
      .build().toMap())
      .execute()
      .body();


    assertEquals(blocks.blocks.size(), 30);
    assertEquals(blocks.blocks.get(0).height, 3);
    assertEquals(blocks.blocks.get(29).height, 2+30);

  }

  @Test
  public void getStatus() throws Exception {
    StatusResponse body = wrapper.blocksAPI.getStatus().execute().body();
    assertFalse(Strings.isNullOrEmpty(body.broadhash));
    assertEquals(
      "cd8171332c012514864edd8eb6f68fc3ea6cb2afbaf21c56e12751022684cea5",
      body.nethash
    );
    assertNotNull(body.epoch);
    assertThat(body.fee, not(0));
    assertThat(body.height, not(0));
    assertThat(body.milestone, not(0));
    assertNotNull(body.reward);
    assertNotNull(body.supply);
  }

  @Test
  public void getAllFees() throws Exception {
    FeesResponse body = wrapper.blocksAPI.getAllFees().execute().body();
    assertNotNull(body.fees);
    assertNotNull(body.fromHeight);
//    assertNotNull(body.toHeight);

    assertEquals(body.fees.send, 10000000);
    assertEquals(body.fees.vote, 100000000);
    assertEquals(body.fees.secondsignature, 500000000);
  }

  @Test
  public void getHeight() throws Exception {
    HeightResponse body = wrapper.blocksAPI.getHeight().execute().body();
    assertThat(body.height, not(0));
    assertThat(body.height, greaterThan(1000000));
  }

}
