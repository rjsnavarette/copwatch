class CreateEmailTemplates < ActiveRecord::Migration[6.0]
  def change
    create_table :email_templates do |t|
      t.string :subject
      t.string :greetings
      t.string :content
      t.string :closing
      t.integer :category_id

      t.timestamps
    end
    add_index :email_templates, :category_id
  end
end
