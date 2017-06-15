package workflowGen;
import java.util.ArrayList;
import java.util.List;



class RandomItemChooser {

	private static ArrayList<Item> items;
	public static void main (String []args)
	{
		RandomItemChooser ric = new RandomItemChooser();
		items = new ArrayList<Item>();
		items.add(new Operation(5,"Like"));
		items.add(new Operation(0.2,"Retweet"));
		items.add(new Operation(0.2,"Reply"));
		System.out.println(ric.chooseOnWeight(items).getOperation());
		System.out.println(ric.chooseOnWeight(items).getOperation());
		System.out.println(ric.chooseOnWeight(items).getOperation());
		System.out.println(ric.chooseOnWeight(items).getOperation());
		System.out.println(ric.chooseOnWeight(items).getOperation());
		System.out.println(ric.chooseOnWeight(items).getOperation());
		System.out.println(ric.chooseOnWeight(items).getOperation());
		System.out.println(ric.chooseOnWeight(items).getOperation());
		System.out.println(ric.chooseOnWeight(items).getOperation());
		System.out.println(ric.chooseOnWeight(items).getOperation());
		System.out.println(ric.chooseOnWeight(items).getOperation());
		System.out.println(ric.chooseOnWeight(items).getOperation());
		System.out.println(ric.chooseOnWeight(items).getOperation());
		System.out.println(ric.chooseOnWeight(items).getOperation());
		System.out.println(ric.chooseOnWeight(items).getOperation());
		System.out.println(ric.chooseOnWeight(items).getOperation());

	}
    public Item chooseOnWeight(List<Item> items) {
        double completeWeight = 0.0;
        for (Item item : items)
            completeWeight += item.getWeight();
        double r = Math.random() * completeWeight;
        double countWeight = 0.0;
        for (Item item : items) {
            countWeight += item.getWeight();
            if (countWeight >= r)
                return item;
        }
        throw new RuntimeException("Should never be shown.");
    }
}