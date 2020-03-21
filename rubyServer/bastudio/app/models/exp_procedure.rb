class ExpProcedure < ApplicationRecord
  belongs_to :expert
  belongs_to :procedure

  def self.qwer
    return "Hello"
  end
end
