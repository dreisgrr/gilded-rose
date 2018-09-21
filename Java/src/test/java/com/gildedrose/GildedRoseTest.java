package com.gildedrose;

import static org.junit.Assert.*;

import org.junit.Test;

public class GildedRoseTest {
    final int MAX_QUALITY_VALUE = 50;

    private Item[] itemsArray = {
        new Item("Backstage passes to a TAFKAL80ETC concert", 5, 20)
    };

    private boolean isPositive(int number) {
        System.out.print("number: "+number);
        System.out.println("result: "+(number>=0));
        return number >= 0;
    }

    private boolean confirmDecreaseInValue(int start, int end) {
        System.out.println("start: "+start);
        System.out.println("end: "+end);
        return start > end;
    }
    
    private int[] getStartingQuality(Item[] items) {
        int[] startingQuality = new int[items.length];
        for(int i = 0; i < items.length; i++) {
            startingQuality[i] = items[i].quality;
        }
        return startingQuality;
    }

    private int[] getStartingSellIn(Item[] items) {
        int[] startingSellIn = new int[items.length];
        for(int i = 0; i < items.length; i++) {
            startingSellIn[i] = items[i].sellIn;
        }
        return startingSellIn;
    }

    public int getDifference(int start, int end) {
        System.out.print("start: "+start);
        System.out.println("end: "+end);
        return start - end;
    }

    @Test
    public void qualityIsNeverNegative() {
        Item[] items = new Item[] { new Item("foo", 0, 20) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertTrue(isPositive(app.items[0].quality));
    }

    @Test
    public void itemsHaveSellInValue() {
        Item[] items = new Item[] { new Item("foo", 5, 20) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        for(Item item : items) {
            assertTrue(isPositive(item.sellIn));
        }
    }

    @Test
    public void itemsHaveQualityValue() {
        Item[] items = new Item[] { new Item("foo", 5, 20) };
        GildedRose app = new GildedRose(itemsArray);
        app.updateQuality();
        for(Item item : app.items) {
            assertTrue(isPositive(item.quality));
        }
    }

    @Test
    public void lowersBothValues() {
        Item[] items = new Item[] { new Item("foo", 0, 20) };
        int[] startingQuality = getStartingQuality(items);
        int[] startingSellIn = getStartingSellIn(items);
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        for (int i=0; i < app.items.length; i++) {
            assertTrue(confirmDecreaseInValue(startingQuality[i], app.items[i].quality));
            assertTrue(confirmDecreaseInValue(startingSellIn[i], app.items[i].sellIn));
        }
    }

    @Test
    public void overSellInDateAndDoubleDegradation() {  
        Item[] items = new Item[] { new Item("foo", 0, 20) };
        int[] startingQuality = getStartingQuality(items);
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        for (int i=0; i < app.items.length; i++) {
            assertFalse(isPositive(app.items[i].sellIn));
            assertEquals(2, getDifference(startingQuality[i], app.items[i].quality));
        }
    }

    @Test
    public void agedBrieHandler() {
        Item[] items = new Item[] { new Item("Aged Brie", 5, 20) };
        int[] startingQuality = getStartingQuality(items);
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        for (int i=0; i < app.items.length; i++) {
            assertEquals("Aged Brie", app.items[i].name);
            assertEquals(-1, getDifference(startingQuality[i], app.items[i].quality));
        }
    }

    @Test
    public void sulfurasHandler() {
        Item[] items = new Item[] { new Item("Sulfuras, Hand of Ragnaros", 0, 80) };
        int[] startingQuality = getStartingQuality(items);
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        for (int i=0; i < app.items.length; i++) {
            assertEquals("Sulfuras, Hand of Ragnaros", app.items[i].name);
            assertEquals(0, getDifference(startingQuality[i], app.items[i].quality));
        }
    }

    @Test
    public void backStagePassesHandler() {
        Item[] items = new Item[] { 
            new Item("Backstage passes to a TAFKAL80ETC concert", 0, 20),
            new Item("Backstage passes to a TAFKAL80ETC concert", 5, 20),
            new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20)
        };
        int[] startingQuality = getStartingQuality(items);
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        for (int i=0; i < app.items.length; i++) {
            assertEquals("Backstage passes to a TAFKAL80ETC concert", app.items[i].name);
            if(app.items[i].sellIn < 0) {
                assertEquals(0, app.items[i].quality);
            }
            if(app.items[i].sellIn < 11 && app.items[i].sellIn > 5) {
                assertEquals(2, getDifference(app.items[i].quality, startingQuality[i]));
            }
            if(app.items[i].sellIn > 0 && app.items[i].sellIn < 6) {
                assertEquals(3, getDifference(app.items[i].quality, startingQuality[i]));
            }
        }
    }

    @Test
    public void notOverMaxLimit() {
        Item[] items = new Item[] { new Item("foo", 14, 50) };
        int[] startingQuality = getStartingQuality(items);
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        for (int i=0; i < app.items.length; i++) {
            assertTrue(app.items[i].quality <= MAX_QUALITY_VALUE);
        }
    }

}
